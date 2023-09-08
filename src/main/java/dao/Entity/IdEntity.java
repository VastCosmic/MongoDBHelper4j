package dao.Entity;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Property;

import java.util.Date;

@Entity(value = "idEntity", useDiscriminator = false)
public class IdEntity {
    @Property("tC")
    private final String tagCode;
    @Property("cT")
    private final Date createdTime;

    public IdEntity(String tagCode) {
        this.tagCode = tagCode;
        this.createdTime = new Date(System.currentTimeMillis());
    }
}
