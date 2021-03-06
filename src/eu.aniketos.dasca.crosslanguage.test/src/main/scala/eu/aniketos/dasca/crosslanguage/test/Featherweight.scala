/*
 * (C) Copyright 2016 The University of Sheffield.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 */

package eu.aniketos.dasca.crosslanguage.test

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import eu.aniketos.dasca.crosslanguage.util.JavaScriptSourceLocation
import eu.aniketos.dasca.crosslanguage.util.JavaSourceLocation
import eu.aniketos.dasca.crosslanguage.util.SourceLocation
import eu.aniketos.dasca.crosslanguage.builder.FilterJavaCallSites
import eu.aniketos.dasca.crosslanguage.builder.MockCordovaExec
import eu.aniketos.dasca.crosslanguage.builder.ReplacePluginDefinesAndRequires
import eu.aniketos.dasca.crosslanguage.builder.FilterJSFrameworks
import eu.aniketos.dasca.crosslanguage.test.Tag._


@RunWith(classOf[JUnitRunner])
class Featherweight extends FlatSpec with Matchers with BeforeAndAfterAll {
   def apkName: String = "de.zertapps.dvhma.featherweight.apk"
   def truePositives = Set[(SourceLocation, SourceLocation)](
            (new JavaSourceLocation(43, "com/borismus/webintent/WebIntent"), new JavaScriptSourceLocation(31, 12, "www/js/index.js"))
           ,(new JavaSourceLocation(145, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"), new JavaScriptSourceLocation(77, 1, "www/js/index.js"))
           ,(new JavaSourceLocation(126, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"), new JavaScriptSourceLocation(77, 1, "www/js/index.js"))
           ,(new JavaSourceLocation(49, "com/borismus/webintent/WebIntent"), new JavaScriptSourceLocation(26, 58, "www/js/index.js"))
           ,(new JavaSourceLocation(49, "com/borismus/webintent/WebIntent"), new JavaScriptSourceLocation(25, 51, "www/js/index.js"))
           ,(new JavaSourceLocation(52, "com/borismus/webintent/WebIntent"), new JavaScriptSourceLocation(31, 12, "www/js/index.js"))
           ,(new JavaSourceLocation(36, "com/borismus/webintent/WebIntent"), new JavaScriptSourceLocation(31, 12, "www/js/index.js"))
           ,(new JavaSourceLocation(85, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"), new JavaScriptSourceLocation(77, 1, "www/js/index.js"))
           ,(new JavaSourceLocation(62, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"), new JavaScriptSourceLocation(46, 21, "www/js/index.js"))
           ,(new JavaSourceLocation(62, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"), new JavaScriptSourceLocation(68, 25, "www/js/index.js"))
           ,(new JavaScriptSourceLocation(7, 12, "www/plugins/de.zertapps.dvhma.plugins.webintent/www/webintent.js"), new JavaSourceLocation(28, "com/borismus/webintent/WebIntent"))
           ,(new JavaScriptSourceLocation(28, 12, "www/plugins/de.zertapps.dvhma.plugins.storage/www/DVHMA-Storage.js"), new JavaSourceLocation(43, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"))
           ,(new JavaScriptSourceLocation(24, 12, "www/plugins/de.zertapps.dvhma.plugins.storage/www/DVHMA-Storage.js"), new JavaSourceLocation(43, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"))
           ,(new JavaScriptSourceLocation(16, 12, "www/plugins/de.zertapps.dvhma.plugins.storage/www/DVHMA-Storage.js"), new JavaSourceLocation(43, "de/zertapps/dvhma/plugins/storage/DVHMAStorage"))
           ,(new JavaScriptSourceLocation(20, 12, "www/plugins/de.zertapps.dvhma.plugins.storage/www/DVHMA-Storage.js"), new JavaSourceLocation(43, "de/zertapps/dvhma/plugins/storage/DVHMAStorage")) 
       )
   def falsePositives = Set[(SourceLocation, SourceLocation)]()
   def falseNegatives = Set[(SourceLocation, SourceLocation)]()
       
   def options = List(
       FilterJavaCallSites
      ,MockCordovaExec
      ,ReplacePluginDefinesAndRequires
      ,FilterJSFrameworks
   )   
       
       
   val app = new AppTest();
   override def beforeAll(){
      app.analyze(apkName, options, truePositives++falseNegatives) // ca. 15sec
   }   
   
   "Test Specification" should "be consistent (truePositives ∩ falsePositives = ∅)" in {
      truePositives intersect falsePositives shouldBe empty
   }
   
   it should "be consistent (truePositives ∩ falseNegatives = falseNegatives)" in {
      truePositives intersect falseNegatives should contain theSameElementsAs falseNegatives
   }
   
   it should "be consistent (falsePositives ∩ falseNegatives = ∅)" in {
      falsePositives intersect falseNegatives shouldBe empty
   }
   
   "Merged CallGraph" should "contain 7.0k nodes" in {
     app.getCallGraphSize() should be (7018 +- 200)
   }
   
   "JavaScriptCallGraph" should "contain 0.8k nodes" in {
     app.getJSCallGraphSize() should be (856 +- 10)
   }

   "JavaCallGraph" should "be contain 6.1k nodes" in {
     app.getJavaCallGraphSize() should be (6162 +- 200)
   }
   
   
   "Java -> JavaScript" should "report ten hits" taggedAs (ManuallyChecked) in {
       app.getJava2JSHits() should be (10)
   }
   
   it should "report no misses" taggedAs (ManuallyChecked) in {
       app.getJava2JSMisses() should be (0)
   }
   
   it should "report no errors" taggedAs (ManuallyChecked) in {
       app.getJava2JSErrors() should be (0)
   }

   it should "have a precision of 100%" taggedAs (ManuallyChecked) in {
       app.getJava2JSPrecision() should be (100.0 +- 0.1)
   }
   
   it should "have a recall of 100%" taggedAs (ManuallyChecked) in {
       app.getJava2JSRecall() should be (100.0 +- 0.1)
   } 
   
   "JavaScript -> Java" should "report five hits" taggedAs (ManuallyChecked) in {
       app.getJS2JavaHits() should be (5)
   }
   
   it should "report no misses" taggedAs (ManuallyChecked) in {
       app.getJS2JavaMisses() should be (0)
   }
   
   it should "report no errors" taggedAs (ManuallyChecked) in {
       app.getJS2JavaErrors() should be (0)
   }

   it should "have a precision of 100%" taggedAs (ManuallyChecked) in {
       app.getJS2JavaPrecision() should be (100.0 +- 0.1)
   }
   
   it should "have a recall of 100%" taggedAs (ManuallyChecked) in {
       app.getJS2JavaRecall() should be (100.0 +- 0.1)
   }
   
   
   "Reported connections" should "contain all expected connections" taggedAs (ManuallyChecked) in {
       app.getTruePositives() should contain theSameElementsAs truePositives
   }
   
   it should "not contain false negatives" taggedAs (ManuallyChecked) in {
      app.getFalseNegatives() shouldBe empty
   }

   it should "not contain false positives" taggedAs (ManuallyChecked) in {
      app.getFalsePositives() shouldBe empty
   }
   
}











