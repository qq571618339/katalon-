import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import org.junit.After

import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import internal.GlobalVariable as GlobalVariable
import groovy.json.JsonSlurper
import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.TestObjectProperty
import groovy.json.JsonOutput

//request = WS.sendRequest(findTestObject('Object Repository/zeus/getAccountByNameAndUserNameUsingPOST',
//	[('userName') : userName,
//		('tenantCode') : tenantCode]))

def str = CustomKeywords.'org.xforceplus.katalon.TokenUtils.getBodyToken'(findTestObject('incomeAPI/login3.0',
	[('userName') : userName,
		('tenantCode') : tenantCode]))



def str1 = CustomKeywords.'org.xforceplus.katalon.TokenUtils.getAccessToken'(findTestObject('incomeAPI/login3.0',
	[('userName') : userName,
		('tenantCode') : tenantCode]))

println str1

def str2 = CustomKeywords.'org.xforceplus.katalon.TokenUtils.getInvoiceWithOrderOperationToken'(findTestObject('incomeAPI/login3.0',
	[('userName') : userName,
		('tenantCode') : tenantCode]))
str3 = str2.replace("=",":")
println str3

//def operation = 'j0201000: $$BqjB0HZ02xlLP0MaflCQTphtorvFP6JB6SW18ydOquw.$$kBtkTtmGTz7Es9sk1TKm6tz2VhNfX8w9ym3Vo5RGx-k.$$bSQDmyJQXdU-0738BpJA-sbdZi8vU4nJeH-BXqj0kPw.$$9t5NpN37GJv7jg5B4lN5wa6C9KiTqMp2j2jI_IWx88o.$$ro-0Tai5-YEL3T6saB2GKrJAhakMC3eXKdX3d-jYdQs.$$ZDX--ybtiQoIZ1VbyTeYweKFknn-1GiBqbbDkvNT92g.$$JS85-KPJe2Ywvb-Qvm1g7eNyo3XaoTHLi6hKTXYlf-o.$$dEy__zyWTuG0bfuvZpEDJW2oSSs3ptECKv8EVQ28s2I.$$gT67-RyxhQgM4C03B3tqilJXU1kk29cyGrequ_fyeEk.$$92tLfldWCJ-FkKzJSftitTrfQz84HAhIbjoJY9mLJpc.$$N583RolsUuQUEuAITH8zEFdcenY8_jyydV-98szpSok.$$DaBDREoFdM4eRnajMoTYEszKs_4nEk6Fa4rcz852ujE.WzJdPT0y$$Z0-3niFSGfKFwtTkdMbMliofYnTd0ybdY1U7YlmOsiU.$$kx-eyx3Lb58W1ls6EfSqXeb5KF4qHjqN7ZJ-d94PfAs.$$_SOmV_TIJ8f27Y5SDJw48XEnNKVrZeNGmyppCyCMqoU.$$leT15R6kwkq2jQUoC5P78NdkTD4Mu-u87cj6Prn1xRI'
def request = CustomKeywords.'org.xforceplus.katalon.TokenUtils.setToken'(findTestObject('Object Repository/NumberQuery'), str1 ,str3)


response = WS.sendRequest(request)

println response.getResponseText()

//response1 = WS.sendRequest(findTestObject('Object Repository/NumberQuery'))




