/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.commerce.model;

import com.liferay.portal.kernel.bean.AutoEscape;
import com.liferay.portal.kernel.exception.LocaleException;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.LocalizedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;

import java.math.BigDecimal;

import java.util.Date;
import java.util.Locale;
import java.util.Map;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the CommerceOrderItem service. Represents a row in the &quot;CommerceOrderItem&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.model.impl.CommerceOrderItemModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.model.impl.CommerceOrderItemImpl</code>.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CommerceOrderItem
 * @generated
 */
@ProviderType
public interface CommerceOrderItemModel
	extends BaseModel<CommerceOrderItem>, GroupedModel, LocalizedModel,
			MVCCModel, ShardedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a commerce order item model instance should use the {@link CommerceOrderItem} interface instead.
	 */

	/**
	 * Returns the primary key of this commerce order item.
	 *
	 * @return the primary key of this commerce order item
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this commerce order item.
	 *
	 * @param primaryKey the primary key of this commerce order item
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this commerce order item.
	 *
	 * @return the mvcc version of this commerce order item
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this commerce order item.
	 *
	 * @param mvccVersion the mvcc version of this commerce order item
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the external reference code of this commerce order item.
	 *
	 * @return the external reference code of this commerce order item
	 */
	@AutoEscape
	public String getExternalReferenceCode();

	/**
	 * Sets the external reference code of this commerce order item.
	 *
	 * @param externalReferenceCode the external reference code of this commerce order item
	 */
	public void setExternalReferenceCode(String externalReferenceCode);

	/**
	 * Returns the commerce order item ID of this commerce order item.
	 *
	 * @return the commerce order item ID of this commerce order item
	 */
	public long getCommerceOrderItemId();

	/**
	 * Sets the commerce order item ID of this commerce order item.
	 *
	 * @param commerceOrderItemId the commerce order item ID of this commerce order item
	 */
	public void setCommerceOrderItemId(long commerceOrderItemId);

	/**
	 * Returns the group ID of this commerce order item.
	 *
	 * @return the group ID of this commerce order item
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this commerce order item.
	 *
	 * @param groupId the group ID of this commerce order item
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this commerce order item.
	 *
	 * @return the company ID of this commerce order item
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this commerce order item.
	 *
	 * @param companyId the company ID of this commerce order item
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this commerce order item.
	 *
	 * @return the user ID of this commerce order item
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this commerce order item.
	 *
	 * @param userId the user ID of this commerce order item
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this commerce order item.
	 *
	 * @return the user uuid of this commerce order item
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this commerce order item.
	 *
	 * @param userUuid the user uuid of this commerce order item
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this commerce order item.
	 *
	 * @return the user name of this commerce order item
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this commerce order item.
	 *
	 * @param userName the user name of this commerce order item
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this commerce order item.
	 *
	 * @return the create date of this commerce order item
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this commerce order item.
	 *
	 * @param createDate the create date of this commerce order item
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this commerce order item.
	 *
	 * @return the modified date of this commerce order item
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this commerce order item.
	 *
	 * @param modifiedDate the modified date of this commerce order item
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the commerce order ID of this commerce order item.
	 *
	 * @return the commerce order ID of this commerce order item
	 */
	public long getCommerceOrderId();

	/**
	 * Sets the commerce order ID of this commerce order item.
	 *
	 * @param commerceOrderId the commerce order ID of this commerce order item
	 */
	public void setCommerceOrderId(long commerceOrderId);

	/**
	 * Returns the commerce price list ID of this commerce order item.
	 *
	 * @return the commerce price list ID of this commerce order item
	 */
	public long getCommercePriceListId();

	/**
	 * Sets the commerce price list ID of this commerce order item.
	 *
	 * @param commercePriceListId the commerce price list ID of this commerce order item
	 */
	public void setCommercePriceListId(long commercePriceListId);

	/**
	 * Returns the c product ID of this commerce order item.
	 *
	 * @return the c product ID of this commerce order item
	 */
	public long getCProductId();

	/**
	 * Sets the c product ID of this commerce order item.
	 *
	 * @param CProductId the c product ID of this commerce order item
	 */
	public void setCProductId(long CProductId);

	/**
	 * Returns the cp instance ID of this commerce order item.
	 *
	 * @return the cp instance ID of this commerce order item
	 */
	public long getCPInstanceId();

	/**
	 * Sets the cp instance ID of this commerce order item.
	 *
	 * @param CPInstanceId the cp instance ID of this commerce order item
	 */
	public void setCPInstanceId(long CPInstanceId);

	/**
	 * Returns the parent commerce order item ID of this commerce order item.
	 *
	 * @return the parent commerce order item ID of this commerce order item
	 */
	public long getParentCommerceOrderItemId();

	/**
	 * Sets the parent commerce order item ID of this commerce order item.
	 *
	 * @param parentCommerceOrderItemId the parent commerce order item ID of this commerce order item
	 */
	public void setParentCommerceOrderItemId(long parentCommerceOrderItemId);

	/**
	 * Returns the quantity of this commerce order item.
	 *
	 * @return the quantity of this commerce order item
	 */
	public int getQuantity();

	/**
	 * Sets the quantity of this commerce order item.
	 *
	 * @param quantity the quantity of this commerce order item
	 */
	public void setQuantity(int quantity);

	/**
	 * Returns the shipped quantity of this commerce order item.
	 *
	 * @return the shipped quantity of this commerce order item
	 */
	public int getShippedQuantity();

	/**
	 * Sets the shipped quantity of this commerce order item.
	 *
	 * @param shippedQuantity the shipped quantity of this commerce order item
	 */
	public void setShippedQuantity(int shippedQuantity);

	/**
	 * Returns the json of this commerce order item.
	 *
	 * @return the json of this commerce order item
	 */
	@AutoEscape
	public String getJson();

	/**
	 * Sets the json of this commerce order item.
	 *
	 * @param json the json of this commerce order item
	 */
	public void setJson(String json);

	/**
	 * Returns the name of this commerce order item.
	 *
	 * @return the name of this commerce order item
	 */
	public String getName();

	/**
	 * Returns the localized name of this commerce order item in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param locale the locale of the language
	 * @return the localized name of this commerce order item
	 */
	@AutoEscape
	public String getName(Locale locale);

	/**
	 * Returns the localized name of this commerce order item in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param locale the local of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this commerce order item. If <code>useDefault</code> is <code>false</code> and no localization exists for the requested language, an empty string will be returned.
	 */
	@AutoEscape
	public String getName(Locale locale, boolean useDefault);

	/**
	 * Returns the localized name of this commerce order item in the language. Uses the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @return the localized name of this commerce order item
	 */
	@AutoEscape
	public String getName(String languageId);

	/**
	 * Returns the localized name of this commerce order item in the language, optionally using the default language if no localization exists for the requested language.
	 *
	 * @param languageId the ID of the language
	 * @param useDefault whether to use the default language if no localization exists for the requested language
	 * @return the localized name of this commerce order item
	 */
	@AutoEscape
	public String getName(String languageId, boolean useDefault);

	@AutoEscape
	public String getNameCurrentLanguageId();

	@AutoEscape
	public String getNameCurrentValue();

	/**
	 * Returns a map of the locales and localized names of this commerce order item.
	 *
	 * @return the locales and localized names of this commerce order item
	 */
	public Map<Locale, String> getNameMap();

	/**
	 * Sets the name of this commerce order item.
	 *
	 * @param name the name of this commerce order item
	 */
	public void setName(String name);

	/**
	 * Sets the localized name of this commerce order item in the language.
	 *
	 * @param name the localized name of this commerce order item
	 * @param locale the locale of the language
	 */
	public void setName(String name, Locale locale);

	/**
	 * Sets the localized name of this commerce order item in the language, and sets the default locale.
	 *
	 * @param name the localized name of this commerce order item
	 * @param locale the locale of the language
	 * @param defaultLocale the default locale
	 */
	public void setName(String name, Locale locale, Locale defaultLocale);

	public void setNameCurrentLanguageId(String languageId);

	/**
	 * Sets the localized names of this commerce order item from the map of locales and localized names.
	 *
	 * @param nameMap the locales and localized names of this commerce order item
	 */
	public void setNameMap(Map<Locale, String> nameMap);

	/**
	 * Sets the localized names of this commerce order item from the map of locales and localized names, and sets the default locale.
	 *
	 * @param nameMap the locales and localized names of this commerce order item
	 * @param defaultLocale the default locale
	 */
	public void setNameMap(Map<Locale, String> nameMap, Locale defaultLocale);

	/**
	 * Returns the sku of this commerce order item.
	 *
	 * @return the sku of this commerce order item
	 */
	@AutoEscape
	public String getSku();

	/**
	 * Sets the sku of this commerce order item.
	 *
	 * @param sku the sku of this commerce order item
	 */
	public void setSku(String sku);

	/**
	 * Returns the unit price of this commerce order item.
	 *
	 * @return the unit price of this commerce order item
	 */
	public BigDecimal getUnitPrice();

	/**
	 * Sets the unit price of this commerce order item.
	 *
	 * @param unitPrice the unit price of this commerce order item
	 */
	public void setUnitPrice(BigDecimal unitPrice);

	/**
	 * Returns the promo price of this commerce order item.
	 *
	 * @return the promo price of this commerce order item
	 */
	public BigDecimal getPromoPrice();

	/**
	 * Sets the promo price of this commerce order item.
	 *
	 * @param promoPrice the promo price of this commerce order item
	 */
	public void setPromoPrice(BigDecimal promoPrice);

	/**
	 * Returns the discount amount of this commerce order item.
	 *
	 * @return the discount amount of this commerce order item
	 */
	public BigDecimal getDiscountAmount();

	/**
	 * Sets the discount amount of this commerce order item.
	 *
	 * @param discountAmount the discount amount of this commerce order item
	 */
	public void setDiscountAmount(BigDecimal discountAmount);

	/**
	 * Returns the final price of this commerce order item.
	 *
	 * @return the final price of this commerce order item
	 */
	public BigDecimal getFinalPrice();

	/**
	 * Sets the final price of this commerce order item.
	 *
	 * @param finalPrice the final price of this commerce order item
	 */
	public void setFinalPrice(BigDecimal finalPrice);

	/**
	 * Returns the discount percentage level1 of this commerce order item.
	 *
	 * @return the discount percentage level1 of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel1();

	/**
	 * Sets the discount percentage level1 of this commerce order item.
	 *
	 * @param discountPercentageLevel1 the discount percentage level1 of this commerce order item
	 */
	public void setDiscountPercentageLevel1(
		BigDecimal discountPercentageLevel1);

	/**
	 * Returns the discount percentage level2 of this commerce order item.
	 *
	 * @return the discount percentage level2 of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel2();

	/**
	 * Sets the discount percentage level2 of this commerce order item.
	 *
	 * @param discountPercentageLevel2 the discount percentage level2 of this commerce order item
	 */
	public void setDiscountPercentageLevel2(
		BigDecimal discountPercentageLevel2);

	/**
	 * Returns the discount percentage level3 of this commerce order item.
	 *
	 * @return the discount percentage level3 of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel3();

	/**
	 * Sets the discount percentage level3 of this commerce order item.
	 *
	 * @param discountPercentageLevel3 the discount percentage level3 of this commerce order item
	 */
	public void setDiscountPercentageLevel3(
		BigDecimal discountPercentageLevel3);

	/**
	 * Returns the discount percentage level4 of this commerce order item.
	 *
	 * @return the discount percentage level4 of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel4();

	/**
	 * Sets the discount percentage level4 of this commerce order item.
	 *
	 * @param discountPercentageLevel4 the discount percentage level4 of this commerce order item
	 */
	public void setDiscountPercentageLevel4(
		BigDecimal discountPercentageLevel4);

	/**
	 * Returns the unit price with tax amount of this commerce order item.
	 *
	 * @return the unit price with tax amount of this commerce order item
	 */
	public BigDecimal getUnitPriceWithTaxAmount();

	/**
	 * Sets the unit price with tax amount of this commerce order item.
	 *
	 * @param unitPriceWithTaxAmount the unit price with tax amount of this commerce order item
	 */
	public void setUnitPriceWithTaxAmount(BigDecimal unitPriceWithTaxAmount);

	/**
	 * Returns the promo price with tax amount of this commerce order item.
	 *
	 * @return the promo price with tax amount of this commerce order item
	 */
	public BigDecimal getPromoPriceWithTaxAmount();

	/**
	 * Sets the promo price with tax amount of this commerce order item.
	 *
	 * @param promoPriceWithTaxAmount the promo price with tax amount of this commerce order item
	 */
	public void setPromoPriceWithTaxAmount(BigDecimal promoPriceWithTaxAmount);

	/**
	 * Returns the discount with tax amount of this commerce order item.
	 *
	 * @return the discount with tax amount of this commerce order item
	 */
	public BigDecimal getDiscountWithTaxAmount();

	/**
	 * Sets the discount with tax amount of this commerce order item.
	 *
	 * @param discountWithTaxAmount the discount with tax amount of this commerce order item
	 */
	public void setDiscountWithTaxAmount(BigDecimal discountWithTaxAmount);

	/**
	 * Returns the final price with tax amount of this commerce order item.
	 *
	 * @return the final price with tax amount of this commerce order item
	 */
	public BigDecimal getFinalPriceWithTaxAmount();

	/**
	 * Sets the final price with tax amount of this commerce order item.
	 *
	 * @param finalPriceWithTaxAmount the final price with tax amount of this commerce order item
	 */
	public void setFinalPriceWithTaxAmount(BigDecimal finalPriceWithTaxAmount);

	/**
	 * Returns the discount percentage level1 with tax amount of this commerce order item.
	 *
	 * @return the discount percentage level1 with tax amount of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel1WithTaxAmount();

	/**
	 * Sets the discount percentage level1 with tax amount of this commerce order item.
	 *
	 * @param discountPercentageLevel1WithTaxAmount the discount percentage level1 with tax amount of this commerce order item
	 */
	public void setDiscountPercentageLevel1WithTaxAmount(
		BigDecimal discountPercentageLevel1WithTaxAmount);

	/**
	 * Returns the discount percentage level2 with tax amount of this commerce order item.
	 *
	 * @return the discount percentage level2 with tax amount of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel2WithTaxAmount();

	/**
	 * Sets the discount percentage level2 with tax amount of this commerce order item.
	 *
	 * @param discountPercentageLevel2WithTaxAmount the discount percentage level2 with tax amount of this commerce order item
	 */
	public void setDiscountPercentageLevel2WithTaxAmount(
		BigDecimal discountPercentageLevel2WithTaxAmount);

	/**
	 * Returns the discount percentage level3 with tax amount of this commerce order item.
	 *
	 * @return the discount percentage level3 with tax amount of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel3WithTaxAmount();

	/**
	 * Sets the discount percentage level3 with tax amount of this commerce order item.
	 *
	 * @param discountPercentageLevel3WithTaxAmount the discount percentage level3 with tax amount of this commerce order item
	 */
	public void setDiscountPercentageLevel3WithTaxAmount(
		BigDecimal discountPercentageLevel3WithTaxAmount);

	/**
	 * Returns the discount percentage level4 with tax amount of this commerce order item.
	 *
	 * @return the discount percentage level4 with tax amount of this commerce order item
	 */
	public BigDecimal getDiscountPercentageLevel4WithTaxAmount();

	/**
	 * Sets the discount percentage level4 with tax amount of this commerce order item.
	 *
	 * @param discountPercentageLevel4WithTaxAmount the discount percentage level4 with tax amount of this commerce order item
	 */
	public void setDiscountPercentageLevel4WithTaxAmount(
		BigDecimal discountPercentageLevel4WithTaxAmount);

	/**
	 * Returns the subscription of this commerce order item.
	 *
	 * @return the subscription of this commerce order item
	 */
	public boolean getSubscription();

	/**
	 * Returns <code>true</code> if this commerce order item is subscription.
	 *
	 * @return <code>true</code> if this commerce order item is subscription; <code>false</code> otherwise
	 */
	public boolean isSubscription();

	/**
	 * Sets whether this commerce order item is subscription.
	 *
	 * @param subscription the subscription of this commerce order item
	 */
	public void setSubscription(boolean subscription);

	/**
	 * Returns the delivery group of this commerce order item.
	 *
	 * @return the delivery group of this commerce order item
	 */
	@AutoEscape
	public String getDeliveryGroup();

	/**
	 * Sets the delivery group of this commerce order item.
	 *
	 * @param deliveryGroup the delivery group of this commerce order item
	 */
	public void setDeliveryGroup(String deliveryGroup);

	/**
	 * Returns the shipping address ID of this commerce order item.
	 *
	 * @return the shipping address ID of this commerce order item
	 */
	public long getShippingAddressId();

	/**
	 * Sets the shipping address ID of this commerce order item.
	 *
	 * @param shippingAddressId the shipping address ID of this commerce order item
	 */
	public void setShippingAddressId(long shippingAddressId);

	/**
	 * Returns the printed note of this commerce order item.
	 *
	 * @return the printed note of this commerce order item
	 */
	@AutoEscape
	public String getPrintedNote();

	/**
	 * Sets the printed note of this commerce order item.
	 *
	 * @param printedNote the printed note of this commerce order item
	 */
	public void setPrintedNote(String printedNote);

	/**
	 * Returns the requested delivery date of this commerce order item.
	 *
	 * @return the requested delivery date of this commerce order item
	 */
	public Date getRequestedDeliveryDate();

	/**
	 * Sets the requested delivery date of this commerce order item.
	 *
	 * @param requestedDeliveryDate the requested delivery date of this commerce order item
	 */
	public void setRequestedDeliveryDate(Date requestedDeliveryDate);

	/**
	 * Returns the booked quantity ID of this commerce order item.
	 *
	 * @return the booked quantity ID of this commerce order item
	 */
	public long getBookedQuantityId();

	/**
	 * Sets the booked quantity ID of this commerce order item.
	 *
	 * @param bookedQuantityId the booked quantity ID of this commerce order item
	 */
	public void setBookedQuantityId(long bookedQuantityId);

	/**
	 * Returns the manually adjusted of this commerce order item.
	 *
	 * @return the manually adjusted of this commerce order item
	 */
	public boolean getManuallyAdjusted();

	/**
	 * Returns <code>true</code> if this commerce order item is manually adjusted.
	 *
	 * @return <code>true</code> if this commerce order item is manually adjusted; <code>false</code> otherwise
	 */
	public boolean isManuallyAdjusted();

	/**
	 * Sets whether this commerce order item is manually adjusted.
	 *
	 * @param manuallyAdjusted the manually adjusted of this commerce order item
	 */
	public void setManuallyAdjusted(boolean manuallyAdjusted);

	@Override
	public String[] getAvailableLanguageIds();

	@Override
	public String getDefaultLanguageId();

	@Override
	public void prepareLocalizedFieldsForImport() throws LocaleException;

	@Override
	public void prepareLocalizedFieldsForImport(Locale defaultImportLocale)
		throws LocaleException;

}