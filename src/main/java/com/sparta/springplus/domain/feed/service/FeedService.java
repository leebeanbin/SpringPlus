package com.sparta.springplus.domain.feed.service;

import com.querydsl.core.Tuple;
import com.sparta.springplus.domain.feed.dto.GetEachFeedDto;
import com.sparta.springplus.domain.likes.FeedLikes;
import com.sparta.springplus.domain.likes.repository.FeedLikesRepository;
import com.sparta.springplus.domain.user.service.UserService;
import com.sparta.springplus.global.enums.ErrorType;
import com.sparta.springplus.global.enums.ResponseMessage;
import com.sparta.springplus.global.exception.CustomException;
import com.sparta.springplus.domain.feed.dto.FeedRequestDto;
import com.sparta.springplus.domain.feed.dto.FeedResponseDto;
import com.sparta.springplus.domain.feed.dto.GroupFeedResponseDto;
import com.sparta.springplus.domain.user.dto.ResponseEntityDto;
import com.sparta.springplus.domain.feed.Feed;
import com.sparta.springplus.domain.user.User;
import com.sparta.springplus.domain.feed.repository.FeedRepository;
import com.sparta.springplus.domain.user.repository.UserRepository;
import com.sparta.springplus.global.security.UserDetailsImpl;
import com.sparta.springplus.global.security.UserDetailsServiceImpl;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedService {

    private final UserDetailsServiceImpl userDetailsService;
    private final FeedRepository feedRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    /**
     * 게시물 작성
     *
     * @param requestDto 게시물
     * @param user       사용자 정보
     * @return 게시물 작성 내용 반환
     */
    @Transactional
    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user) {
        User userFromRepo = userRepository.findById(user.getId()).orElseThrow(
                () -> new CustomException(ErrorType.NOT_EXISTS_USER)
        );

        if(!Objects.equals(userFromRepo.getUsername(),userDetailsService.loadUserByUsername(user.getUsername()).getUsername())){
            throw new CustomException(ErrorType.NOT_EXISTS_USER);
        }

        Feed feed = requestDto.toEntity(userFromRepo);
        Feed savedFeed = feedRepository.save(feed);
        return new FeedResponseDto(savedFeed);
    }

    public GetEachFeedDto getFeed(Long feedId) {
        Feed feed = getFeedAndFindUser(feedId);

        return new GetEachFeedDto(feed);
    }


    @Transactional(readOnly = true)
    public Page<GetEachFeedDto> getLikedFeedsWithPage(UserDetailsImpl userDetails, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return feedRepository.findLikedFeedsByUserId(userDetails.getUser().getId(), pageable);
    }

    /**
     * 게시물 조회
     *
     * @param page 페이지수
     * @param size 사이즈
     * @return 게시물 조회
     */
    @Transactional(readOnly = true)
    public Page<FeedResponseDto> getFeedsWithPage(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        return feedRepository.findAll(pageable)
                .map(FeedResponseDto::new); // `Page<Feed>`를 `Page<FeedResponseDto>`로 변환
    }

    /**
     * 게시물 수정
     *
     * @param feedId     게시물 아이디
     * @param requestDto 게시물 엔티티 정보
     * @param user       사용자 엔티티 정보
     * @return 게시물 수정한 거 반환
     * @throws CustomException 게시물 여부와 사용자 검증
     */
    @Transactional
    public ResponseEntityDto<FeedResponseDto> updateFeed(Long feedId, FeedRequestDto requestDto,
            User user) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));
        // 작성자 본인만 수정 가능
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorType.NOT_AUTHORIZED_UPDATE);
        }
        feed.update(requestDto.getTitle(), requestDto.getContent());
        FeedResponseDto feedResponse = new FeedResponseDto(feed);
        return new ResponseEntityDto<>(ResponseMessage.FEED_UPDATE_SUCCESS, feedResponse);
    }

    /**
     * 게시물 삭제
     *
     * @param userId 사용자 아이디
     * @param feedId 게시물 아이디
     * @return 게시물 삭제 응답 반환
     * @throws CustomException 게시물 존재 여부, 사용자 검증
     */
    @Transactional
    public ResponseEntityDto<Void> deleteFeed(Long userId, Long feedId) {
        Feed feed = feedRepository.findById(feedId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        // 작성자 검증
        if (!feed.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorType.NOT_AUTHORIZED_DELETE);
        }

        feedRepository.delete(feed);
        return new ResponseEntityDto<>(ResponseMessage.FEED_DELETE_SUCCESS, null);
    }

    /**
     * 소속별 게시물 조회
     *
     * @param userId 사용자 아이디
     * @param page   페이지 수
     * @param size   사이즈
     * @return 게시물 조회 반환
     * @throws CustomException 사용자 검증
     */
    @Transactional(readOnly = true)
    public List<GroupFeedResponseDto> getFeedsByUserCompany(Long userId, int page, int size) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_FEED));

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return feedRepository.findByUser_Company(user.getCompany(), pageable)
                .getContent()
                .stream()
                .map(GroupFeedResponseDto::new)
                .collect(Collectors.toList());
    }

    /**
     * 로그인한 회원의 팔로잉 회원 게시글 목록 조회
     *
     * @param user 로그인한 회원
     * @param page 페이지
     * @param size 크기
     * @return 게시글 목록
     */
    public List<FeedResponseDto> getFollowingFeed(User user, int page, int size) {
        User fromUser = userService.getUserById(user.getId());

        List<Long> followList = fromUser.getFollowingList().stream()
                .map(f -> f.getToUser().getId())
                .toList();

        if (followList.isEmpty()) {
            throw new CustomException(ErrorType.NOT_FOUND_FEED);
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return feedRepository.findAllByUserIdIn(followList, pageable)
                .getContent()
                .stream()
                .map(FeedResponseDto::new)
                .toList();
    }

    private Feed getFeedAndFindUser(Long feedId) {
        // feedId를 통한 게시물 조회
        Feed feed = feedRepository.findById(feedId).orElseThrow(
                () -> new CustomException(ErrorType.NOT_FOUND_FEED)
        );

        // Feed에 저장된 user의 값을 통해서 userRepository에 search를 통해서 검증
        userRepository.findUserByUsername(feed.getUser().getUsername()).orElseThrow(
                () -> new CustomException(ErrorType.NOT_EXISTS_USER)
        );
        return feed;
    }
}
