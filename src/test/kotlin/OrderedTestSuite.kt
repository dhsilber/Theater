import com.mobiletheatertech.plot.tests.XmlTest
import org.junit.runner.RunWith
import org.junit.runners.Suite
import tests.BackupTest
import tests.entities.LuminaireTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
//  TagRegistryTest::class,
//  StartupTest::class,
//  WallButtonTest::class,
//  WriteSvgButtonTest::class,
//  CreateWithElementTest::class,
//  IntegrationTest::class,
//  WallTest::class,
//  PointTest::class,
  LuminaireTest::class,
  XmlTest::class,
  BackupTest::class,
)
class OrderedTestSuite {
}
