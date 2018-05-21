package net.vpc.scholar.hadrumaths.io;

import java.io.IOException;

public class DefaultStringSerializer implements StringSerializer {

    public static final StringSerializer[] BASE_SERIALIZERS = {IdStringSerializer.INSTANCE, SimpleStringSerializer.INSTANCE, Base64StringSerializer.INSTANCE};

    @Override
    public boolean acceptDeserialize(String value) {
        for (StringSerializer stringSerializer : BASE_SERIALIZERS) {
            if (stringSerializer.acceptDeserialize(value)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int acceptSerialize(Object value) {
        int stringSerializerBestLevel = 0;
        for (StringSerializer stringSerializer : BASE_SERIALIZERS) {
            int ii = stringSerializer.acceptSerialize(value);
            if (ii > 0 && ii > stringSerializerBestLevel) {
                stringSerializerBestLevel = ii;
            }
        }
        return stringSerializerBestLevel;
    }

    @Override
    public String serialize(Object value) throws IOException {
        StringSerializer stringSerializerBest = null;
        int stringSerializerBestLevel = 0;
        for (StringSerializer stringSerializer : BASE_SERIALIZERS) {
            int ii = stringSerializer.acceptSerialize(value);
            if (ii > 0 && ii > stringSerializerBestLevel) {
                stringSerializerBestLevel = ii;
                stringSerializerBest = stringSerializer;
            }
        }
        if (stringSerializerBest == null) {
            throw new IllegalArgumentException("Unable to serialize " + value);
        }
        return stringSerializerBest.serialize(value);
    }

    @Override
    public Object deserialize(String value) throws IOException {
        for (StringSerializer stringSerializer : BASE_SERIALIZERS) {
            if (stringSerializer.acceptDeserialize(value)) {
                return stringSerializer.deserialize(value);
            }
        }
        throw new IllegalArgumentException("Unable to deserialize " + value);
    }
}
