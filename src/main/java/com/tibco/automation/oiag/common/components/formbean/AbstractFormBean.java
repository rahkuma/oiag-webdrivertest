package com.tibco.automation.oiag.common.components.formbean;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.openqa.selenium.WebElement;

import com.tibco.automation.oiag.common.components.ExtendedChoiceButton;
import com.tibco.automation.oiag.common.components.ExtendedDropDown;
import com.tibco.automation.oiag.common.components.ExtendedInputBox;
import com.tibco.automation.oiag.common.components.ExtendedRadioButton;
import com.tibco.automation.oiag.common.components.formbean.FormElement.Type;
import com.tibco.automation.oiag.common.framework.webdriver.ExtendedWebDriver;
import com.tibco.automation.oiag.common.framework.webdriver.WebDriverManager;
import com.tibco.automation.oiag.common.utils.LocatorUtil;
import com.tibco.automation.oiag.common.utils.StringUtil;
import com.tibco.automation.oiag.common.utils.RandomStringGenerator.RandomizerTypes;

public class AbstractFormBean {

	public void fillUiData(String... fieldLocs) {

		List<String> includeLst = null;
		if ((fieldLocs != null) && (fieldLocs.length > 0)) {
			includeLst = Arrays.asList(fieldLocs);
		}

		Field[] fields = getAllFields(this.getClass());

		Arrays.sort(fields, new FieldsComparator());

		for (Field field : fields) {
			if (field.isAnnotationPresent(FormElement.class)
					&& ((includeLst == null) || includeLst.contains(field.getName())
							|| includeLst.contains(field.getAnnotation(FormElement.class).fieldLoc()))) {
				fillData(field);
			}
		}
	}

	private Field[] getAllFields(Class<?> clazz) {
		Collection<Field> fields = new HashSet<Field>();
		fields.addAll(Arrays.asList(clazz.getDeclaredFields()));

		return fields.toArray(new Field[] {});
	}

	private void fillData(Field field) {

		FormElement params = field.getAnnotation(FormElement.class);

		Type type = params.fieldType();
		String loc = params.fieldLoc();
		String val = params.defaultValue();
		String attVal = params.attributeLoc();

		try {
			field.setAccessible(true);
			Object o = field.get(this);
			val = (null == o ? val : String.valueOf(o));
		} catch (NullPointerException e) {
			val = null;
		} catch (Exception e) {
			System.err.println("Unable to get data from bean for " + field.getName());
		}

		try {
			Method m = getMethod(this.getClass(), "fill" + StringUtil.getTitleCase(field.getName()));
			m.setAccessible(true);
			m.invoke(this);

		} catch (NoSuchMethodException nse) {
			if (!params.required() && (val == null || val == "" || val.isEmpty())) {
				return;
			} else {
				fillValue(loc, val, type, attVal, WebDriverManager.getDriver());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Unable to fill element : " + field.getName());
		}

	}

	public static Method getMethod(Class<?> clazz, String name) throws NoSuchMethodException {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method m : methods) {
			if (m.getName().equalsIgnoreCase(name)) {
				return m;
			}
		}
		if (null != clazz.getSuperclass()) {
			getMethod(clazz.getSuperclass(), name);
		}
		throw new NoSuchMethodException();
	}

	private class FieldsComparator implements Comparator<Field> {

		@Override
		public int compare(Field o1, Field o2) {
			return getOrder(o1) - getOrder(o2);
		}

		private int getOrder(Field f) {
			return f.isAnnotationPresent(FormElement.class) ? f.getAnnotation(FormElement.class).order()
					: Integer.MAX_VALUE;
		}

	}

	protected void fillValue(String loc, String val, Type type, String attLoc, ExtendedWebDriver driver) {
		switch (type) {
		case textbox:
		case textarea:
			ExtendedInputBox txt = new ExtendedInputBox(LocatorUtil.getBy(loc));
			txt.click();
			txt.clear();
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
			}
			txt.sendKeys(val);
			break;
		case file:
			new ExtendedInputBox(LocatorUtil.getBy(loc)).sendKeys(val);
			break;
		case checkbox:
			ExtendedChoiceButton ele = new ExtendedChoiceButton(LocatorUtil.getBy(loc));
			boolean isChecked = ele.isSelected();
			if (Boolean.valueOf(val) != isChecked) {
				ele.click();
			}
			break;
		case optionbox:
			ExtendedRadioButton optionbox = new ExtendedRadioButton(LocatorUtil.getBy(loc));
			boolean isSelected = optionbox.isSelected();
			if (Boolean.valueOf(val) != isSelected) {
				optionbox.click();
			}
			break;
		case selectbox:
			try {
				ExtendedDropDown dropDown = new ExtendedDropDown(LocatorUtil.getBy(loc));
				driver.getWaitUtility().waitForElementPresent(dropDown.getBy());
				dropDown.select(val);
			} catch (Exception e) {
				e.printStackTrace();
			}
			driver.getWaitUtility().waitForElementNotPresent("//span[@id='waitMsg']/ancestor::div[@id='PopupDiv']");
			break;
		case multiselectbox:
			if (val.equals("")) {
				try {
					ExtendedDropDown dropDown = new ExtendedDropDown(LocatorUtil.getBy(loc));
					dropDown.click();
					driver.getWaitUtility().waitForElementPresent(dropDown.getBy());
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					loc = loc.replace("''", "'" + val + "'");
					ExtendedDropDown dropDown = new ExtendedDropDown(LocatorUtil.getBy(loc));
					dropDown.selectThenClick(loc);
					driver.getWaitUtility().waitForElementPresent(dropDown.getBy());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case toggletbox:
			WebElement elementAttribute = driver.findElement(LocatorUtil.getBy(attLoc));
			WebElement element = driver.findElement(LocatorUtil.getBy(loc));
			String checkBox = elementAttribute.getAttribute("checked");

			String check = "";
			if (checkBox != null) {
				check = checkBox;
			} else {
				check = "false";
			}

			if (val.equals(check)) {

			} else {
				element.click();
			}
			break;
		default:
			break;
		}
	}

	public void fillRandomData() {
		// Field[] fields = this.getClass().getDeclaredFields();
		Field[] fields = getAllFields(this.getClass());
		for (Field field : fields) {
			// logger.debug("NAME :: " + field.getName());
			if (!(Modifier.isFinal(field.getModifiers()))) {
				RandomizerTypes type = RandomizerTypes.MIXED;
				int len = 10;
				long min = 0, max = 0;
				String prefix = "", suffix = "";
				boolean skip = false;
				String format = "";
				Randomizer randomizer = field.getAnnotation(Randomizer.class);
				if (randomizer != null) {
					type = field.getType() == Date.class ? RandomizerTypes.DIGITS_ONLY : randomizer.type();
					len = randomizer.length();
					prefix = randomizer.prefix();
					suffix = randomizer.suffix();
					min = randomizer.minval();
					max = randomizer.maxval();
					skip = randomizer.skip();
					format = randomizer.format();
				}
				if (skip) {
					continue;
				}
				if (randomizer != null) {
					String str = StringUtil.isBlank(format) ? RandomStringUtils.random(len,
							!type.equals(RandomizerTypes.DIGITS_ONLY), !type.equals(RandomizerTypes.LETTERS_ONLY))
							: StringUtil.getRandomString(format);

					try {
						// deal with IllegalAccessException
						field.setAccessible(true);

						if ((field.getType() == String.class)) {
							if ((min == max) && (min == 0)) {
								str = StringUtil.isBlank(format)
										? RandomStringUtils.random(len, !type.equals(RandomizerTypes.DIGITS_ONLY),
												!type.equals(RandomizerTypes.LETTERS_ONLY))
										: StringUtil.getRandomString(format);

							} else {
								str = String.valueOf((int) (Math.random() * (max - min + 1)) + min);

							}
							String rStr = prefix + str + suffix;
							field.set(this, rStr);
						} else {
							String rStr = "";
							if ((min == max) && (min == 0)) {
								rStr = RandomStringUtils.random(len, false, true);
							} else {
								rStr = String.valueOf((int) (Math.random() * (max - min + 1)) + min);
							}

							// Double d = Double.valueOf(rStr);
							if (field.getType() == Integer.TYPE) {
								field.setInt(this, Integer.parseInt(rStr));
							} else if (field.getType() == Float.TYPE) {
								field.setFloat(this, Float.parseFloat(rStr));

							} else if (field.getType() == Double.TYPE) {
								field.setDouble(this, Double.parseDouble(rStr));

							} else if (field.getType() == Long.TYPE) {
								field.setLong(this, Long.parseLong(rStr));

							} else if (field.getType() == Short.TYPE) {
								field.setShort(this, Short.parseShort(rStr));
							} else if (field.getType() == Date.class) {
								long days = Long.parseLong(rStr);
								field.set(this, new Date(System.currentTimeMillis() + days * 1000 * 60 * 60 * 24));
							} else if (field.getType() == Boolean.TYPE) {
								// field.setBoolean(this,
								// Boolean.parseBoolean(rStr));
								field.setBoolean(this, RandomUtils.nextBoolean());

							}
						}

					} catch (IllegalArgumentException e) {
						System.err.println("Unable to fill random data in field " + field.getName());
					} catch (IllegalAccessException e) {
						System.err.println("Unable to Access " + field.getName());
					}
				}
			}

		}

	}

}
