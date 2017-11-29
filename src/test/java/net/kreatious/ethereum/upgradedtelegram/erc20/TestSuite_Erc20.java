package net.kreatious.ethereum.upgradedtelegram.erc20;

import net.kreatious.ethereum.upgradedtelegram.erc20.scenario.ApproveAllowanceAndTransferFromTest_Negative;
import net.kreatious.ethereum.upgradedtelegram.erc20.scenario.ApproveAllowanceAndTransferFromTest_Positive;
import net.kreatious.ethereum.upgradedtelegram.erc20.scenario.TransferTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Runs the ERC20 tests
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    TransferTest.class,
    ApproveAllowanceAndTransferFromTest_Positive.class,
    ApproveAllowanceAndTransferFromTest_Negative.class
})
public class TestSuite_Erc20 {
}
