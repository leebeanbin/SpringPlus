package com.sparta.springplus.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1378103237L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final com.sparta.springplus.domain.common.QTimeStamp _super = new com.sparta.springplus.domain.common.QTimeStamp(this);

    public final com.sparta.springplus.domain.company.QCompany company;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final ListPath<com.sparta.springplus.domain.follow.Follow, com.sparta.springplus.domain.follow.QFollow> followerList = this.<com.sparta.springplus.domain.follow.Follow, com.sparta.springplus.domain.follow.QFollow>createList("followerList", com.sparta.springplus.domain.follow.Follow.class, com.sparta.springplus.domain.follow.QFollow.class, PathInits.DIRECT2);

    public final ListPath<com.sparta.springplus.domain.follow.Follow, com.sparta.springplus.domain.follow.QFollow> followingList = this.<com.sparta.springplus.domain.follow.Follow, com.sparta.springplus.domain.follow.QFollow>createList("followingList", com.sparta.springplus.domain.follow.Follow.class, com.sparta.springplus.domain.follow.QFollow.class, PathInits.DIRECT2);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath intro = createString("intro");

    public final StringPath nickname = createString("nickname");

    public final StringPath password = createString("password");

    public final BooleanPath refresh = createBoolean("refresh");

    public final EnumPath<com.sparta.springplus.global.enums.Status> status = createEnum("status", com.sparta.springplus.global.enums.Status.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final StringPath username = createString("username");

    public final EnumPath<com.sparta.springplus.global.enums.UserRole> userRole = createEnum("userRole", com.sparta.springplus.global.enums.UserRole.class);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.company = inits.isInitialized("company") ? new com.sparta.springplus.domain.company.QCompany(forProperty("company")) : null;
    }

}

