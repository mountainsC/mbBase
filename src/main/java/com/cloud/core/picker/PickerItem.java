package com.cloud.core.picker;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

/**
 * @Author lijinghuan
 * @Email:ljh0576123@163.com
 * @CreateTime:2018/4/27
 * @Description:数据项
 * @Modifier:
 * @ModifyContent:
 */
@Entity(nameInDb = "tb_address")
public class PickerItem {
    /**
     * 节点id
     */
    @Property(nameInDb = "id")
    @Id
    private int id = 0;
    /**
     * 父节点id
     */
    @Property(nameInDb = "parentId")
    private int parentId = 0;
    /**
     * 节点名称
     */
    @Property(nameInDb = "name")
    @Index
    private String name = "";

    @Generated(hash = 1328124205)
    public PickerItem(int id, int parentId, String name) {
        this.id = id;
        this.parentId = parentId;
        this.name = name;
    }

    @Generated(hash = 967523974)
    public PickerItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
