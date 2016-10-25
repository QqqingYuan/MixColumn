package cn.kejso.Mix;

import cn.kejso.AbstractTransform;

import java.util.List;

/**
 * Created by dell on 2016/10/25.
 */
public class AdjColumn {
    private String table;
    private String new_table;
    private Field key_column;
    private Field side_column;

    private AbstractTransform transform;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getNew_table() {
        return new_table;
    }

    public void setNew_table(String new_table) {
        this.new_table = new_table;
    }

    public Field getKey_column() {
        return key_column;
    }

    public void setKey_column(Field key_column) {
        this.key_column = key_column;
    }

    public Field getSide_column() {
        return side_column;
    }

    public void setSide_column(Field side_column) {
        this.side_column = side_column;
    }

    public AbstractTransform getTransform() {
        return transform;
    }
    public void setTransform(AbstractTransform transform) {
        this.transform = transform;
    }
}
