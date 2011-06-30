/*
 *  Straight - A system to manage financial demands for small and decentralized
 *  organizations.
 *  Copyright (C) 2011  Octahedron 
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package br.octahedron.straight.ui;

import java.util.Random;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Vítor Avelino
 * 
 */
public class LoginLogoutIT extends WebDriverTestClass {

	private static String email = "test" + new Random().nextInt() * 10 + "@example.com";

	@BeforeClass
	public static void addUser() {
		driver.get("http://localhost:8080/");
		// do login
		WebElement imageLogin = driver.findElement(By.linkText("login"));
		imageLogin.click();
		WebElement emailTextInput = driver.findElement(By.id("email"));
		emailTextInput.clear();
		emailTextInput.sendKeys(email);
		WebElement loginButton = driver.findElement(By.xpath("//input[@value='Log In']"));
		loginButton.click();

		Assert.assertEquals("http://localhost:8080/user/new", driver.getCurrentUrl());
		// filling out the form
		WebElement nameTextInput = driver.findElement(By.xpath("//input[@name='name']"));
		nameTextInput.sendKeys("Fulano");
		WebElement phoneNumberTextInput = driver.findElement(By.xpath("//input[@name='phoneNumber']"));
		phoneNumberTextInput.sendKeys("83 8888 8888");
		WebElement descriptionTextInput = driver.findElement(By.xpath("//textarea"));
		descriptionTextInput.sendKeys("Descrição do usuário");
		WebElement submitInput = driver.findElement(By.xpath("//input[@type='submit']"));
		submitInput.submit();

		driver.manage().deleteAllCookies();
	}

	@Before
	public void doLogin() {
		driver.get("http://localhost:8080/");
		WebElement imageLogin = driver.findElement(By.linkText("login"));
		imageLogin.click();
		WebElement emailTextInput = driver.findElement(By.id("email"));
		emailTextInput.clear();
		emailTextInput.sendKeys(email);
		WebElement loginButton = driver.findElement(By.xpath("//input[@value='Log In']"));
		loginButton.click();
	}

	public void testLogin() {
		// after login viewing dashboard
		Assert.assertEquals("http://localhost:8080/dashboard", driver.getCurrentUrl());
	}

	public void testIndexAlreadyLogged() {
		// even trying to visit root url
		driver.get("http://localhost:8080/");
		// it will be redirected to dashboard
		Assert.assertEquals("http://localhost:8080/dashboard", driver.getCurrentUrl());
	}

	@Test
	public void testLogoutDashboard() {
		driver.get("http://localhost:8080/dashboard");
		this.doLogout();
		Assert.assertEquals("http://localhost:8080/", driver.getCurrentUrl());
	}

	@Test
	public void testLogoutBank() {
		driver.navigate().to("http://localhost:8080/bank");
		this.doLogout();
		Assert.assertEquals("http://localhost:8080/", driver.getCurrentUrl());
	}

	@Test
	public void testLogoutServices() {
		driver.get("http://localhost:8080/services");
		this.doLogout();
		Assert.assertEquals("http://localhost:8080/", driver.getCurrentUrl());
	}

	private void doLogout() {
		WebElement logoutLink = driver.findElement(By.linkText("logout"));
		logoutLink.click();
	}

}
