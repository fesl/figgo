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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * @author Vítor Avelino
 * 
 */
public class NewUserIT extends WebDriverTestClass {

	@Before
	public void doLogin() {
		driver.get("http://localhost:8080/");
		WebElement loginLink = driver.findElement(By.linkText("login"));
		loginLink.click();
		WebElement emailTextInput = driver.findElement(By.id("email"));
		emailTextInput.clear();
		emailTextInput.sendKeys("test" + new Random().nextInt() * 100 + "@example.com");
		WebElement loginButton = driver.findElement(By.xpath("//input[@value='Log In']"));
		loginButton.click();
	}

	@After
	public void after() {
		driver.manage().deleteAllCookies();
	}

	@Test
	public void testRegisterUser() {
		// after login with new user, viewing register form
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
		// viewing dashboard after registration
		Assert.assertEquals("http://localhost:8080/dashboard", driver.getCurrentUrl());
	}

	@Test
	public void testLoginWithoutRegister() {
		// after login with new user, viewing register form
		Assert.assertEquals("http://localhost:8080/user/new", driver.getCurrentUrl());
		// ignore it
		driver.get("http://localhost:8080/");
		// viewing register form again
		Assert.assertEquals("http://localhost:8080/user/new", driver.getCurrentUrl());
		// try to hack the system
		driver.get("http://localhost:8080/dashboard");
		// viewing register form again
		Assert.assertEquals("http://localhost:8080/user/new", driver.getCurrentUrl());
	}
}
