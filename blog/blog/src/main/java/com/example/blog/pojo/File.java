package com.example.blog.pojo;
import lombok.Getter;
import lombok.Setter;
@Getter
public class File {
    @Setter
    private int fId;
    @Setter
    private int uId;
    @Setter
    private String url;
    @Setter
    private int bId;

    // 构造方法（可以根据需要增加或修改）
    public File() {
    }

    public File(int uId, String url, int bId) {
        this.uId = uId;
        this.url = url;
        this.bId = bId;
    }

    // 可选的其他方法，例如 toString(), equals(), hashCode() 等

    @Override
    public String toString() {
        return "File{" +
                "fId=" + fId +
                ", uId=" + uId +
                ", url='" + url + '\'' +
                ", bId=" + bId +
                '}';
    }
}

