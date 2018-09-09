package demo.bean;

import lombok.Data;

import java.security.Permission;
import java.util.UUID;

/**
 * 权限对象，模仿session来实现的
 * @author tensor
 */
@Data
public class Authority extends Permission {

    private String ID;
    private String role;

    /**
     * Constructs a permission with the specified name.
     *
     * @param name name of the Permission object being created.
     */
    public Authority(String name) {
        super(name);
        ID = UUID.randomUUID().toString();
    }

    public Authority(String name, String role) {
        this(name);
        this.role = role;
    }

    @Override
    public boolean implies(Permission permission) {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public String getActions() {
        return null;
    }
}
