package uk.snowhunter.pismo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import uk.snowhunter.pismo.controllers.AccountsController;

@SpringBootTest
class PismoApplicationTests {

    @Autowired
    private AccountsController accountsController;

    @Test
	public void contextLoads() {
        assertNotNull(accountsController);
    }


}
