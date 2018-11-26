package TestGenerator; 

import java.util.Collection;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

import domain.Report;
import services.ReportService;
import utilities.AbstractTest;
@ContextConfiguration(locations = {"classpath:spring/junit.xml", "classpath:spring/datasource.xml", "classpath:spring/config/packages.xml"}) 
@RunWith(SpringJUnit4ClassRunner.class) 
@Transactional 
public class ReportServiceTest extends AbstractTest { 

@Autowired 
private ReportService	reportService; 

@Test 
public void saveReportTest(){ 
Report report, saved;
Collection<Report> reports;
report = reportService.findAll().iterator().next();
report.setVersion(57);
saved = reportService.save(report);
reports = reportService.findAll();
Assert.isTrue(reports.contains(saved));
} 

@Test 
public void findAllReportTest() { 
Collection<Report> result; 
result = reportService.findAll(); 
Assert.notNull(result); 
} 

@Test 
public void findOneReportTest(){ 
Report report = reportService.findAll().iterator().next(); 
int reportId = report.getId(); 
Assert.isTrue(reportId != 0); 
Report result; 
result = reportService.findOne(reportId); 
Assert.notNull(result); 
} 

@Test 
public void deleteReportTest() { 
Report report = reportService.findAll().iterator().next(); 
Assert.notNull(report); 
Assert.isTrue(report.getId() != 0); 
Assert.isTrue(this.reportService.exists(report.getId())); 
this.reportService.delete(report); 
} 

} 
