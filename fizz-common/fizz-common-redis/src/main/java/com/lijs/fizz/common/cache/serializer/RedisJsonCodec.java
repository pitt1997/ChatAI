package com.lijs.fizz.common.cache.serializer;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;
import com.fasterxml.jackson.databind.type.TypeFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.util.CharsetUtil;
import org.redisson.client.codec.BaseCodec;
import org.redisson.client.protocol.Decoder;
import org.redisson.client.protocol.Encoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.datatype.XMLGregorianCalendar;
import java.io.IOException;
import java.io.OutputStream;

public class RedisJsonCodec extends BaseCodec {

    public static final RedisJsonCodec INSTANCE = new RedisJsonCodec();
    private static final Logger logger = LoggerFactory.getLogger(RedisJsonCodec.class);

    private final ObjectMapper objectMapper;
    private final Encoder encoder;
    private final Decoder<Object> decoder;

    public RedisJsonCodec() {
        this(new ObjectMapper());
    }

    public RedisJsonCodec(ClassLoader classLoader) {
        this(createObjectMapper(classLoader, new ObjectMapper()));
    }

    public RedisJsonCodec(ClassLoader classLoader, RedisJsonCodec codec) {
        this(createObjectMapper(classLoader, codec.objectMapper.copy()));
    }

    public RedisJsonCodec(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper.copy();
        this.encoder = createEncoder();
        this.decoder = createDecoder();
        init(this.objectMapper);
        initTypeInclusion(this.objectMapper);
    }

    protected static ObjectMapper createObjectMapper(ClassLoader classLoader, ObjectMapper om) {
        TypeFactory tf = TypeFactory.defaultInstance().withClassLoader(classLoader);
        om.setTypeFactory(tf);
        return om;
    }

    private Encoder createEncoder() {
        return in -> {
            ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
            try (ByteBufOutputStream os = new ByteBufOutputStream(out)) {
                if (in instanceof String) {
                    out.writeCharSequence(in.toString(), CharsetUtil.UTF_8);
                } else {
                    objectMapper.writeValue((OutputStream) os, in);
                }
                return os.buffer();
            } catch (IOException e) {
                out.release();
                throw e;
            }
        };
    }

    private Decoder<Object> createDecoder() {
        return (buf, state) -> {
            String stringBuf = buf.toString(CharsetUtil.UTF_8);
            logger.debug("Decoding stringBuf: {}", stringBuf);
            if (stringBuf.startsWith("{") || stringBuf.startsWith("[")) {
                return objectMapper.readValue(stringBuf, Object.class);
            } else {
                return stringBuf;
            }
        };
    }

    private void initTypeInclusion(ObjectMapper objectMapper) {
        TypeResolverBuilder<?> mapTyper = new ObjectMapper.DefaultTypeResolverBuilder(ObjectMapper.DefaultTyping.NON_FINAL) {
            public boolean useForType(JavaType t) {
                switch (this._appliesFor) {
                    case NON_CONCRETE_AND_ARRAYS:
                        while (t.isArrayType()) {
                            t = t.getContentType();
                        }
                    case OBJECT_AND_NON_CONCRETE:
                        break;
                    case NON_FINAL:
                        while (t.isArrayType()) {
                            t = t.getContentType();
                        }
                        if (t.getRawClass() == Long.class) {
                            return true;
                        }
                        if (t.getRawClass() == XMLGregorianCalendar.class) {
                            return false;
                        }
                        return !t.isFinal();
                    default:
                        return t.getRawClass() == Object.class;
                }
                return t.getRawClass() == Object.class || !t.isConcrete();
            }
        };
        mapTyper.init(JsonTypeInfo.Id.CLASS, null);
        mapTyper.inclusion(JsonTypeInfo.As.PROPERTY);
        objectMapper.setDefaultTyping(mapTyper);
    }

    private void init(ObjectMapper objectMapper) {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.setVisibility(objectMapper.getSerializationConfig()
                .getDefaultVisibilityChecker()
                .withFieldVisibility(JsonAutoDetect.Visibility.ANY)
                .withGetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withSetterVisibility(JsonAutoDetect.Visibility.NONE)
                .withCreatorVisibility(JsonAutoDetect.Visibility.NONE));
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.enable(JsonGenerator.Feature.WRITE_BIGDECIMAL_AS_PLAIN);
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper.enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        objectMapper.addMixIn(Throwable.class, ThrowableMixIn.class);
    }

    @Override
    public Decoder<Object> getValueDecoder() {
        return this.decoder;
    }

    @Override
    public Encoder getValueEncoder() {
        return this.encoder;
    }

    public ClassLoader getClassLoader() {
        return this.objectMapper.getTypeFactory().getClassLoader() != null ? this.objectMapper.getTypeFactory().getClassLoader() : super.getClassLoader();
    }

    public ObjectMapper getObjectMapper() {
        return this.objectMapper;
    }

    @JsonIdentityInfo(
            generator = ObjectIdGenerators.IntSequenceGenerator.class,
            property = "@id"
    )
    @JsonAutoDetect(
            fieldVisibility = JsonAutoDetect.Visibility.ANY,
            getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY,
            setterVisibility = JsonAutoDetect.Visibility.NONE,
            isGetterVisibility = JsonAutoDetect.Visibility.NONE
    )
    public static class ThrowableMixIn {
        public ThrowableMixIn() {
        }
    }

}
