import com.gigaspaces.document.SpaceDocument
import com.gigaspaces.metadata.SpaceTypeDescriptorBuilder
import com.j_spaces.core.client.SQLQuery
import demo.EngineerPojo
import org.openspaces.admin.Admin
import org.openspaces.admin.AdminFactory
import org.openspaces.core.GigaSpace
import org.fusesource.jansi.AnsiConsole
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.concurrent.TimeUnit

public class Demo {


    SpaceDocument document;

    EngineerPojo engineer;

    private void writePojos(GigaSpace gigaSpace) {
        printCommands('' +
                '   gigaSpace.write(new EngineerPojo(123, "Me", "groovy"));\n' +
                '   gigaSpace.write(new EngineerPojo(345, "you", "java"));');
        println ""
        println ""
        gigaSpace.write(new EngineerPojo(123, "Me", "groovy"));
        gigaSpace.write(new EngineerPojo(345, "you", "java"));
    }

    private void writeDocument1(GigaSpace gigaSpace) {
        printCommands('' +
                '   document = new SpaceDocument("demo.EngineerPojo");\n' +
                '   document.setProperty("id", 789);\n' +
                '   document.setProperty("name", "he");\n' +
                '   document.setProperty("age", 21);\n' +
                '   gigaSpace.write(document);');
        println ""
        println ""

        document.setProperty("id", 789);
        document.setProperty("name", "he");
        document.setProperty("age", 21);
        gigaSpace.write(document);
    }

    private void writeDocument2(GigaSpace gigaSpace) {
        printCommands('' +
                '   document.setProperty("id", 743);\n' +
                '   document.setProperty("name", "she");\n' +
                '   document.setProperty("age", 21);\n' +
                '   gigaSpace.write(document);');
        println ""
        println ""

        document.setProperty("id", 743);
        document.setProperty("name", "she");
        document.setProperty("age", 21);
        gigaSpace.write(document);
    }

    private void testReadPojo(GigaSpace gigaSpace) {
        printCommands('' +
                '   engineer = gigaSpace.read(new EngineerPojo(123));\n' +
                '   System.out.println(engineer);');
        println ""

        print_color "_YResult: _X"

        engineer = gigaSpace.read(new EngineerPojo(123));
        System.out.println("    "+engineer);
        println ""
        println ""
    }

    private void testReadDocument(GigaSpace gigaSpace) {
        printCommands('' +
                '   document.setProperty("id", 345);\n' +
                '   SpaceDocument engineerDoc = gigaSpace.read(document);\n' +
                '   System.out.println(engineerDoc);');
        println ""

        print_color "_YResult: _X"

        document.setProperty("id", 345);
        SpaceDocument engineerDoc = gigaSpace.read(document);

        System.out.println("    "+engineerDoc);
        println ""
        println ""
    }

    private void testReadSQLQuery(GigaSpace gigaSpace) {
        printCommands('' +
                '   engineer = gigaSpace.read(new SQLQuery<EngineerPojo>(EngineerPojo.class, "id=789 AND name=\'he\'"));\n' +
                '   System.out.println(engineer);');
        println ""

        print_color "_YResult: _X"
        engineer = gigaSpace.read(new SQLQuery<EngineerPojo>(EngineerPojo.class,
                "id=789 AND name='he'"));
        System.out.println("    "+engineer);
        println ""
        println ""
    }

    private void testReadJDBC() throws Exception {
        printCommands('' +
                '   Class.forName("com.j_spaces.jdbc.driver.GDriver");\n' +
                '   Connection connection = null;\n' +
                '   connection = DriverManager.getConnection("jdbc:gigaspaces:url:" + getRemoteSpaceURL());\n' +
                '   Statement statement = connection.createStatement();\n' +
                '   statement.execute("SELECT * FROM demo.EngineerPojo WHERE age=21");');
        println ""

        print_color "_YResults: _X"
        Class.forName("com.j_spaces.jdbc.driver.GDriver");
        Connection connection = null;
        connection = DriverManager.getConnection("jdbc:gigaspaces:url:" + getRemoteSpaceURL());
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM demo.EngineerPojo WHERE age=21");

        ResultSet resultSet = statement.getResultSet();
        int count = 0;
        while (resultSet.next()) {
            count++;
            System.out.println("    JDBC: id=" + resultSet.getInt("id") + " name=" + resultSet.getString("name") + " age=" + resultSet.getInt("age"));
        }
        println ""
        println ""
    }

    private String getRemoteSpaceURL() {
        return "jini://*/*/myDataGrid?locators=" + System.getenv("LOOKUPLOCATORS")+"&groups="+System.getenv("LOOKUPGROUPS");
    }

    private void testReadJDBC2() throws Exception {
        printCommands('' +
                '   Class.forName("com.j_spaces.jdbc.driver.GDriver");\n' +
                '   String url = getRemoteSpaceURL();\n' +
                '   System.out.println("permutation url == " + url);\n' +
                '   Connection connection = DriverManager.getConnection("jdbc:gigaspaces:url:" + url);\n' +
                '   Statement statement = connection.createStatement();\n' +
                '   statement.execute("SELECT * FROM demo.EngineerPojo");');
        println ""

        print_color "_YResult: _X"

        Class.forName("com.j_spaces.jdbc.driver.GDriver");
        String url = getRemoteSpaceURL();
        System.out.println("    permutation url == " + url);
        Connection connection = DriverManager.getConnection("jdbc:gigaspaces:url:" + url);
        Statement statement = connection.createStatement();
        statement.execute("SELECT * FROM demo.EngineerPojo");

        ResultSet resultSet = statement.getResultSet();
        int count = 0;
        while (resultSet.next()) {
            count++;
            System.out.println("    JDBC: id=" + resultSet.getInt("id") + " name=" + resultSet.getString("name") + " age=" + resultSet.getInt("age"));
        }
        println ""
        println ""
    }

    private void printCommands(message) {
        print_color "_YExecuting: _X"
        print_color "_P" + message + "_X"

    }

    public void run(GigaSpace gigaSpace) throws Exception {

        this.document = new SpaceDocument("demo.EngineerPojo");
        gigaSpace.getTypeManager().registerTypeDescriptor(new SpaceTypeDescriptorBuilder("demo.EngineerPojo").idProperty("id").supportsDynamicProperties(true).create())

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in))

        print_color "_BPress ENTER to write Pojo objects_X"
        br.readLine()
        writePojos(gigaSpace);

        print_color "_BPress ENTER to read Pojo objects_X"
        br.readLine()
        testReadPojo(gigaSpace);

        print_color "_BPress ENTER to read Document objects_X"
        br.readLine()
        testReadDocument(gigaSpace);

        print_color "_BPress ENTER to write Document object_X"
        br.readLine()
        writeDocument1(gigaSpace);

        print_color "_BPress ENTER to read using JDBC_X"
        br.readLine()
        testReadJDBC2();

        print_color "_BPress ENTER to read using SQLQuery_X"
        br.readLine()
        testReadSQLQuery(gigaSpace);

        print_color "_BPress ENTER to write Document object_X"
        br.readLine()
        writeDocument2(gigaSpace);

        print_color "_BPress ENTER to read using JDBC_X"
        br.readLine()
        testReadJDBC();

        print_color "_BPress ENTER to exit_X"
        br.readLine()

    }
    static void print_color(text) {
        AnsiConsole.out.println(text.replace('_B', '\u001b[32;1m')
                .replace('_G', '\u001b[32;1m')
                .replace('_R', '\u001b[31;1m')
                .replace('_Y', '\u001b[33;1m')
                .replace('_P', '\u001b[35;1m')
                .replace('_X', '\u001b[0m'))
    }
}


try {
    def lookuplocators = System.getenv("LOOKUPLOCATORS")
	def lookupgroups = System.getenv("LOOKUPGROUPS")
    def gridname = "myDataGrid"
    Admin admin = new AdminFactory().useDaemonThreads(true).addLocators(lookuplocators).addGroups(lookupgroups).createAdmin();
    def pus = admin.getProcessingUnits().waitFor(gridname, 10, TimeUnit.SECONDS);
    if (pus == null) {
        Demo.print_color("_RUnable to find myDataGrid processing unit_X")
        System.exit(1)
    }
    if (! pus.waitFor(1)) {
        Demo.print_color("_RUnable to find myDataGrid instances_X")
        System.exit(1)
    }

    def gigaSpace = admin.getProcessingUnits().getProcessingUnit(gridname).getSpace().getGigaSpace()
    def demo = new Demo()
    demo.run(gigaSpace)

    admin.close();
} catch (Exception e) {
    e.printStackTrace()
    Demo.print_color("_RError occurred: " + e.toString()+"_X")
}
