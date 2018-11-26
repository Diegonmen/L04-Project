
package utilities;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

/*
 * //FIXME En los Services generar los exist, en los Test hacer los test de los @Query de los repositorios
 * Si pulsais Alt + Shift + S y le dais a generate delegate methods se puede generar automaticamente todos los metodos del repositorio
 * en los servicios para no hacerlos como monos ;)
 * */
public class TestGenerator {
	
	static String clase = "EMPTY";
	
	static final String imports = 
			"import java.util.Collection;\r\n" + 
			"\r\n" + 
			"import javax.transaction.Transactional;\r\n" + 
			"\r\n" + 
			"import org.junit.Test;\r\n" + 
			"import org.junit.runner.RunWith;\r\n" + 
			"import org.springframework.beans.factory.annotation.Autowired;\r\n" + 
			"import org.springframework.test.context.ContextConfiguration;\r\n" + 
			"import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;\r\n" + 
			"import org.springframework.util.Assert;\r\n" + 
			"\r\n" + 
			"import domain.%s;\r\n" + 
			"import services.%s;\r\n" + 
			"import utilities.AbstractTest;";

	public static void main(String[] args) {
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		File parent = new File(s + "/src/main/java/domain");
		
		for(File e : parent.listFiles()) {
			clase = e.getName().replace(".java", "");
			
			System.out.println(String.format("generating test of '%s'", clase));
			
			try {
				TestGenerator.test();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void test() throws IOException {
		String objeto = clase.toLowerCase();
		
		Path currentRelativePath = Paths.get("");
		String s = currentRelativePath.toAbsolutePath().toString();
		
		String file = String.format(s + File.separator + "src/test/java/TestGenerator/%sServiceTest.java", clase);
		File archivo = new File(file);
		archivo.delete();
		PrintWriter pw = new PrintWriter(new FileWriter(file));
		try {
			pw.write("package TestGenerator; \n\n");
			
			pw.println(String.format(imports, clase, clase + "Service"));
			
			pw.write("@ContextConfiguration(locations = {\"classpath:spring/junit.xml\", \"classpath:spring/datasource.xml\", \"classpath:spring/config/packages.xml\"}) \n");
			pw.write("@RunWith(SpringJUnit4ClassRunner.class) \n");
			pw.write("@Transactional \n");
			pw.write("public class " + clase + "ServiceTest extends AbstractTest { \n\n");
			pw.write("@Autowired \n");
			pw.write("private " + clase + "Service	" + objeto + "Service; \n\n");

			pw.write("@Test \n");
			pw.write("public void save" + clase + "Test(){ \n");
			pw.write(clase + " " + objeto + ", saved;\n");
			pw.write("Collection<" + clase + "> " + objeto + "s;\n");
			pw.write(objeto + " = " + objeto + "Service.findAll().iterator().next();\n");
			pw.write(objeto + ".setVersion(57);\n");
			pw.write("saved = " + objeto + "Service.save(" + objeto + ");\n");
			pw.write(objeto + "s = " + objeto + "Service.findAll();\n");
			pw.write("Assert.isTrue(" + objeto + "s.contains(saved));\n");
			pw.write("} \n\n");

			pw.write("@Test \n");
			pw.write("public void findAll" + clase + "Test() { \n");
			pw.write("Collection<" + clase + "> result; \n");
			pw.write("result = " + objeto + "Service.findAll(); \n");
			pw.write("Assert.notNull(result); \n");
			pw.write("} \n\n");

			pw.write("@Test \n");
			pw.write("public void findOne" + clase + "Test(){ \n");
			pw.write(clase + " " + objeto + " = " + objeto + "Service.findAll().iterator().next(); \n");
			pw.write("int " + objeto + "Id = " + objeto + ".getId(); \n");
			pw.write("Assert.isTrue(" + objeto + "Id != 0); \n");
			pw.write(clase + " result; \n");
			pw.write("result = " + objeto + "Service.findOne(" + objeto + "Id); \n");
			pw.write("Assert.notNull(result); \n");
			pw.write("} \n\n");

			pw.write("@Test \n");
			pw.write("public void delete" + clase + "Test() { \n");
			pw.write(clase + " " + objeto + " = " + objeto + "Service.findAll().iterator().next(); \n");
			pw.write("Assert.notNull(" + objeto + "); \n");
			pw.write("Assert.isTrue(" + objeto + ".getId() != 0); \n");
			pw.write("Assert.isTrue(this." + objeto + "Service.exists(" + objeto + ".getId())); \n");
			pw.write("this." + objeto + "Service.delete(" + objeto + "); \n");
			pw.write("} \n\n");

			pw.write("} \n");
		} catch (final Exception e) {
			e.printStackTrace();
		}
		if (pw != null)
			pw.close();
	}
}
