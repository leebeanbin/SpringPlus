package com.sparta.springplus.domain.reply;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReply is a Querydsl query type for Reply
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReply extends EntityPathBase<Reply> {

    private static final long serialVersionUID = 1776557213L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReply reply = new QReply("reply");

    public final com.sparta.springplus.domain.common.QTimeStamp _super = new com.sparta.springplus.domain.common.QTimeStamp(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final com.sparta.springplus.domain.feed.QFeed feed;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> likesCount = createNumber("likesCount", Integer.class);

    public final ListPath<com.sparta.springplus.domain.likes.ReplyLikes, com.sparta.springplus.domain.likes.QReplyLikes> likesList = this.<com.sparta.springplus.domain.likes.ReplyLikes, com.sparta.springplus.domain.likes.QReplyLikes>createList("likesList", com.sparta.springplus.domain.likes.ReplyLikes.class, com.sparta.springplus.domain.likes.QReplyLikes.class, PathInits.DIRECT2);

    public final EnumPath<com.sparta.springplus.global.enums.Status> status = createEnum("status", com.sparta.springplus.global.enums.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final com.sparta.springplus.domain.user.QUser user;

    public QReply(String variable) {
        this(Reply.class, forVariable(variable), INITS);
    }

    public QReply(Path<? extends Reply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReply(PathMetadata metadata, PathInits inits) {
        this(Reply.class, metadata, inits);
    }

    public QReply(Class<? extends Reply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.feed = inits.isInitialized("feed") ? new com.sparta.springplus.domain.feed.QFeed(forProperty("feed"), inits.get("feed")) : null;
        this.user = inits.isInitialized("user") ? new com.sparta.springplus.domain.user.QUser(forProperty("user"), inits.get("user")) : null;
    }

}

