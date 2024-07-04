package com.sparta.springplus.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserRefreshToken is a Querydsl query type for UserRefreshToken
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserRefreshToken extends EntityPathBase<UserRefreshToken> {

    private static final long serialVersionUID = -610874301L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserRefreshToken userRefreshToken = new QUserRefreshToken("userRefreshToken");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isInvalid = createBoolean("isInvalid");

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Integer> reissueCount = createNumber("reissueCount", Integer.class);

    public final QUser user;

    public QUserRefreshToken(String variable) {
        this(UserRefreshToken.class, forVariable(variable), INITS);
    }

    public QUserRefreshToken(Path<? extends UserRefreshToken> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserRefreshToken(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserRefreshToken(PathMetadata metadata, PathInits inits) {
        this(UserRefreshToken.class, metadata, inits);
    }

    public QUserRefreshToken(Class<? extends UserRefreshToken> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

