package com.sparta.springplus.domain.user;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserPasswordLog is a Querydsl query type for UserPasswordLog
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserPasswordLog extends EntityPathBase<UserPasswordLog> {

    private static final long serialVersionUID = -276692572L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserPasswordLog userPasswordLog = new QUserPasswordLog("userPasswordLog");

    public final com.sparta.springplus.domain.common.QTimeStamp _super = new com.sparta.springplus.domain.common.QTimeStamp(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath password = createString("password");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final QUser user;

    public QUserPasswordLog(String variable) {
        this(UserPasswordLog.class, forVariable(variable), INITS);
    }

    public QUserPasswordLog(Path<? extends UserPasswordLog> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserPasswordLog(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserPasswordLog(PathMetadata metadata, PathInits inits) {
        this(UserPasswordLog.class, metadata, inits);
    }

    public QUserPasswordLog(Class<? extends UserPasswordLog> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

