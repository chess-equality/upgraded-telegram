package net.kreatious.ethereum.upgradedtelegram.erc20;

import net.kreatious.ethereum.upgradedtelegram.erc20.scenario.ApproveAllowanceAndTransferFromTest;
import net.kreatious.ethereum.upgradedtelegram.erc20.scenario.TransferTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the ERC20 tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TransferTest.class,
    ApproveAllowanceAndTransferFromTest.class
})
public class TestSuite_Erc20 {
}
