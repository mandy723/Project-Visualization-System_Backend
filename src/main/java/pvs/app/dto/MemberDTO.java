package pvs.app.dto;

import lombok.Data;

@Data
public class MemberDTO {

    private Long id;
    private String account;
    private String password;

    @Override
    public String toString() {
        return "MemberBo{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
