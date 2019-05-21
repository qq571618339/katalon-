package org.xforceplus.katalon
import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import com.kms.katalon.core.testobject.HttpBodyContent
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.checkpoint.CheckpointFactory
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testcase.TestCaseFactory
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testdata.TestDataFactory
import com.kms.katalon.core.testobject.ObjectRepository
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords

import internal.GlobalVariable

import MobileBuiltInKeywords as Mobile
import WSBuiltInKeywords as WS
import WebUiBuiltInKeywords as WebUI

import org.openqa.selenium.WebElement
import org.openqa.selenium.WebDriver
import org.openqa.selenium.By
import groovy.json.JsonLexer
import com.kms.katalon.core.mobile.keyword.internal.MobileDriverFactory
import com.kms.katalon.core.webui.driver.DriverFactory

import com.kms.katalon.core.testobject.RequestObject
import com.kms.katalon.core.testobject.ResponseObject
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObjectProperty

import com.kms.katalon.core.mobile.helper.MobileElementCommonHelper
import com.kms.katalon.core.util.KeywordUtil
import groovy.json.JsonToken
import com.kms.katalon.core.webui.exception.WebElementNotFoundException
import groovy.json.JsonSlurper
import org.codehaus.groovy.*

class TokenUtils {
	/**
	 * Send request and verify status code
	 * @param request request object, must be an instance of RequestObject
	 * @param expectedStatusCode
	 * @return a boolean to indicate whether the response status code equals the expected one
	 */
	@Keyword
	def verifyStatusCode(TestObject request, int expectedStatusCode) {
		if (request instanceof RequestObject) {
			RequestObject requestObject = (RequestObject) request
			ResponseObject response = WSBuiltInKeywords.sendRequest(requestObject)
			if (response.getStatusCode() == expectedStatusCode) {
				KeywordUtil.markPassed("Response status codes match")
			} else {
				KeywordUtil.markFailed("Response status code not match. Expected: " +
						expectedStatusCode + " - Actual: " + response.getStatusCode() )
			}
		} else {
			KeywordUtil.markFailed(request.getObjectId() + " is not a RequestObject")
		}
	}

	/**
	 * Add Header basic authorization field,
	 * this field value is Base64 encoded token from user name and password
	 * @param request object, must be an instance of RequestObject
	 * @param username username
	 * @param password password
	 * @return the original request object with basic authorization header field added
	 */
	@Keyword
	def addBasicAuthorizationProperty(TestObject request, String username, String password) {
		if (request instanceof RequestObject) {
			String authorizationValue = username + ":" + password
			authorizationValue = "Basic " + authorizationValue.bytes.encodeBase64().toString()

			// Find available basic authorization field and change its value to the new one, if any
			List<TestObjectProperty> headerProperties = request.getHttpHeaderProperties()
			boolean fieldExist = false
			for (int i = 0; i < headerProperties.size(); i++) {
				TestObjectProperty headerField = headerProperties.get(i)
				if (headerField.getName().equals('Authorization')) {
					KeywordUtil.logInfo("Found existent basic authorization field. Replacing its value.")
					headerField.setValue(authorizationValue)
					fieldExist = true
					break
				}
			}

			if (!fieldExist) {
				TestObjectProperty authorizationProperty = new TestObjectProperty("Authorization",
						ConditionType.EQUALS, authorizationValue, true)
				headerProperties.add(authorizationProperty)
			}
			KeywordUtil.markPassed("Basic authorization field has been added to request header")
		} else {
			KeywordUtil.markFailed(request.getObjectId() + "is not a RequestObject")
		}
		return request
	}

	/*
	 * 获取BodyContent token, X-Access-Token, X-Operation-Token
	 */
	@Keyword
	def getBodyToken(TestObject request){
		if(request instanceof RequestObject){
			RequestObject requestObject = (RequestObject)request
			ResponseObject response = WS.sendRequest(requestObject)
			def bodyProperties = response.getResponseText()
			def str = bodyProperties.replace("\"", "")
			println "${str}"
			def jsonSlurper = new JsonSlurper()
			def jsonResponse = jsonSlurper.parseText(response.getResponseText())
			println jsonResponse.token
			return jsonResponse.token
		}

	}

	@Keyword
	def getAccessToken(TestObject request){
		if(request instanceof RequestObject){
			RequestObject requestObject = (RequestObject)request
			ResponseObject response = WS.sendRequest(request)
			println new JsonSlurper().parseText(response.getResponseText()).token
			return new JsonSlurper().parseText(response.getResponseText()).token
		}
	}

	/*
	 * 发票配单Operation-token
	 */
	@Keyword
	def getInvoiceWithOrderOperationToken(TestObject request){
		if(request instanceof RequestObject){
			List<TestObjectProperty> headerProperties = request.getHttpHeaderProperties()
			Iterator it = headerProperties.iterator()
			while(it.hasNext()){
				println it.next().getValue()
			}
			RequestObject requestObject = (RequestObject)request
			ResponseObject response = WS.sendRequest(requestObject)
			def jsonSlurper = new JsonSlurper()
			def jsonResponse = jsonSlurper.parseText(response.getResponseText())
			println jsonResponse.getClass()
			def operation = jsonResponse.operations
			operation.each{println "${it.key}"}
			operation.each{println "${it.key}:${it.value}"}
			def ope = operation.grep{ "${it.key}"== 'j0201000'}.each{println "${it.key}:${it.value}"}.join()
			println ope.getClass()

			ope
		}
	}


	@Keyword
	def setToken(TestObject request, String accessToken, String operationToken){
		if(request instanceof RequestObject){
			List<TestObjectProperty> headerProperties = request.getHttpHeaderProperties()
			Iterator it = headerProperties.iterator()
			while(it.hasNext()){
				println it.next().getName()
				//				println it.next().getValue()
			}
			boolean accessTokenExist = false
			boolean operationTokenExist = false
			for(int i = 0; i < headerProperties.size(); i++){
				TestObjectProperty headerField = headerProperties.get(i)
				if(headerField.getName().equals('X-Access-Token')){
					println headerField.getValue()
					KeywordUtil.logInfo('found X-Access-Token and set accessToken value')
					headerField.setValue(accessToken)
					println headerField.getValue()
					accessTokenExist = true
				}
				if(headerField.getName().equals('X-Operation-Token')){
					println headerField.getValue()
					KeywordUtil.logInfo('Found X-Operation-Token and set operationToken value')
					headerField.setValue(operationToken)
					println headerField.getValue()
					operationTokenExist = true
				}
			}
			if(!accessTokenExist){
				TestObjectProperty accessTokenProperty = new TestObjectProperty('X-Access-Token', ConditionType.EQUALS, accessToken, true)
				headerProperties.add(accessTokenProperty)
			}
			KeywordUtil.markPassed("the property  X-Access-Token has been added to the request header")

			if(!operationTokenExist){
				TestObjectProperty operationTokenProperty = new TestObjectProperty('X-Operation-Token', ConditionType.EQUALS, operationToken, true)
				headerProperties.add(operationTokenProperty)
			}
			KeywordUtil.markPassed("the Property X-Operation-Token has been added to the request header")
		}else{
			KeywordUtil.markFailed(request.getObjectId() + "is not a RequestObject")
		}
		return request
	}
}
