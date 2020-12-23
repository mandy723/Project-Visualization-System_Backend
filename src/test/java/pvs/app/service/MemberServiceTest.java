package pvs.app.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pvs.app.Application;
import pvs.app.dao.MemberDAO;
import pvs.app.dto.MemberDTO;
import pvs.app.entity.Member;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberDAO mockMemberDAO;

    private final Member member01 = new Member();
    private final MemberDTO member01DTO = new MemberDTO();

    @Test
    public void get() {
        //context
        when(mockMemberDAO.findById(1L))
                .thenReturn(member01);

        //given
        member01.setId(1L);
        member01.setAccount("aaaa");
        member01.setPassword("1234");

        member01DTO.setId(1L);
        member01DTO.setAccount("aaaa");
        member01DTO.setPassword("1234");

        //when
        MemberDTO memberDTO = memberService.get(1L);

        //then
        assertEquals(member01DTO.toString(), memberDTO.toString());
        verify(mockMemberDAO, times(1)).findById(1L);
    }

}
