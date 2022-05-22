import org.junit.runner.RunWith
import org.junit.runners.Suite
import tests.StartupTest
import tests.entities.PipeTest

//https://github.com/junit-team/junit4/wiki/aggregating-tests-in-suites

@RunWith(Suite::class)
@Suite.SuiteClasses(
//  XmlTest::class,
//  TagRegistryTest::class,
  StartupTest::class,
//  WallButtonTest::class,
//  WriteSvgButtonTest::class,
//  CreateWithXmlElementTest::class,
//  WallTest::class,
//  PointTest::class,
//  LuminaireTest::class,
//  BackupTest::class,
//  IntegrationTest::class,
  PipeTest::class,
)
class OrderedTestSuite {
}
