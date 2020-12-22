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
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class MemberServiceTest {
    @Autowired
    private MemberService memberService;

    @MockBean
    private MemberDAO memberDAO;

    private final Member mockMember01 = new Member();
    private final MemberDTO member01DTO = new MemberDTO();

    @Test
    public void get() {
        //given
        mockMember01.setId(1L);
        mockMember01.setAccount("aaaa");
        mockMember01.setPassword("1234");

        member01DTO.setId(1L);
        member01DTO.setAccount("aaaa");
        member01DTO.setPassword("1234");
        //when
        when(memberDAO.findById(1L))
                .thenReturn(mockMember01);
        //expect
        assertEquals(member01DTO.toString(), memberService.get(1L).toString());
        verify(memberDAO, times(1)).findById(1L);
    }

}
