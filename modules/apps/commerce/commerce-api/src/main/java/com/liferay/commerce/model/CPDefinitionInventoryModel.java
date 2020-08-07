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
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.GroupedModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.model.ShardedModel;
import com.liferay.portal.kernel.model.StagedAuditedModel;

import java.util.Date;

import org.osgi.annotation.versioning.ProviderType;

/**
 * The base model interface for the CPDefinitionInventory service. Represents a row in the &quot;CPDefinitionInventory&quot; database table, with each column mapped to a property of this class.
 *
 * <p>
 * This interface and its corresponding implementation <code>com.liferay.commerce.model.impl.CPDefinitionInventoryModelImpl</code> exist only as a container for the default property accessors generated by ServiceBuilder. Helper methods and all application logic should be put in <code>com.liferay.commerce.model.impl.CPDefinitionInventoryImpl</code>.
 * </p>
 *
 * @author Alessio Antonio Rendina
 * @see CPDefinitionInventory
 * @generated
 */
@ProviderType
public interface CPDefinitionInventoryModel
	extends BaseModel<CPDefinitionInventory>, GroupedModel, MVCCModel,
			ShardedModel, StagedAuditedModel {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this interface directly. All methods that expect a cp definition inventory model instance should use the {@link CPDefinitionInventory} interface instead.
	 */

	/**
	 * Returns the primary key of this cp definition inventory.
	 *
	 * @return the primary key of this cp definition inventory
	 */
	public long getPrimaryKey();

	/**
	 * Sets the primary key of this cp definition inventory.
	 *
	 * @param primaryKey the primary key of this cp definition inventory
	 */
	public void setPrimaryKey(long primaryKey);

	/**
	 * Returns the mvcc version of this cp definition inventory.
	 *
	 * @return the mvcc version of this cp definition inventory
	 */
	@Override
	public long getMvccVersion();

	/**
	 * Sets the mvcc version of this cp definition inventory.
	 *
	 * @param mvccVersion the mvcc version of this cp definition inventory
	 */
	@Override
	public void setMvccVersion(long mvccVersion);

	/**
	 * Returns the uuid of this cp definition inventory.
	 *
	 * @return the uuid of this cp definition inventory
	 */
	@AutoEscape
	@Override
	public String getUuid();

	/**
	 * Sets the uuid of this cp definition inventory.
	 *
	 * @param uuid the uuid of this cp definition inventory
	 */
	@Override
	public void setUuid(String uuid);

	/**
	 * Returns the cp definition inventory ID of this cp definition inventory.
	 *
	 * @return the cp definition inventory ID of this cp definition inventory
	 */
	public long getCPDefinitionInventoryId();

	/**
	 * Sets the cp definition inventory ID of this cp definition inventory.
	 *
	 * @param CPDefinitionInventoryId the cp definition inventory ID of this cp definition inventory
	 */
	public void setCPDefinitionInventoryId(long CPDefinitionInventoryId);

	/**
	 * Returns the group ID of this cp definition inventory.
	 *
	 * @return the group ID of this cp definition inventory
	 */
	@Override
	public long getGroupId();

	/**
	 * Sets the group ID of this cp definition inventory.
	 *
	 * @param groupId the group ID of this cp definition inventory
	 */
	@Override
	public void setGroupId(long groupId);

	/**
	 * Returns the company ID of this cp definition inventory.
	 *
	 * @return the company ID of this cp definition inventory
	 */
	@Override
	public long getCompanyId();

	/**
	 * Sets the company ID of this cp definition inventory.
	 *
	 * @param companyId the company ID of this cp definition inventory
	 */
	@Override
	public void setCompanyId(long companyId);

	/**
	 * Returns the user ID of this cp definition inventory.
	 *
	 * @return the user ID of this cp definition inventory
	 */
	@Override
	public long getUserId();

	/**
	 * Sets the user ID of this cp definition inventory.
	 *
	 * @param userId the user ID of this cp definition inventory
	 */
	@Override
	public void setUserId(long userId);

	/**
	 * Returns the user uuid of this cp definition inventory.
	 *
	 * @return the user uuid of this cp definition inventory
	 */
	@Override
	public String getUserUuid();

	/**
	 * Sets the user uuid of this cp definition inventory.
	 *
	 * @param userUuid the user uuid of this cp definition inventory
	 */
	@Override
	public void setUserUuid(String userUuid);

	/**
	 * Returns the user name of this cp definition inventory.
	 *
	 * @return the user name of this cp definition inventory
	 */
	@AutoEscape
	@Override
	public String getUserName();

	/**
	 * Sets the user name of this cp definition inventory.
	 *
	 * @param userName the user name of this cp definition inventory
	 */
	@Override
	public void setUserName(String userName);

	/**
	 * Returns the create date of this cp definition inventory.
	 *
	 * @return the create date of this cp definition inventory
	 */
	@Override
	public Date getCreateDate();

	/**
	 * Sets the create date of this cp definition inventory.
	 *
	 * @param createDate the create date of this cp definition inventory
	 */
	@Override
	public void setCreateDate(Date createDate);

	/**
	 * Returns the modified date of this cp definition inventory.
	 *
	 * @return the modified date of this cp definition inventory
	 */
	@Override
	public Date getModifiedDate();

	/**
	 * Sets the modified date of this cp definition inventory.
	 *
	 * @param modifiedDate the modified date of this cp definition inventory
	 */
	@Override
	public void setModifiedDate(Date modifiedDate);

	/**
	 * Returns the cp definition ID of this cp definition inventory.
	 *
	 * @return the cp definition ID of this cp definition inventory
	 */
	public long getCPDefinitionId();

	/**
	 * Sets the cp definition ID of this cp definition inventory.
	 *
	 * @param CPDefinitionId the cp definition ID of this cp definition inventory
	 */
	public void setCPDefinitionId(long CPDefinitionId);

	/**
	 * Returns the cp definition inventory engine of this cp definition inventory.
	 *
	 * @return the cp definition inventory engine of this cp definition inventory
	 */
	@AutoEscape
	public String getCPDefinitionInventoryEngine();

	/**
	 * Sets the cp definition inventory engine of this cp definition inventory.
	 *
	 * @param CPDefinitionInventoryEngine the cp definition inventory engine of this cp definition inventory
	 */
	public void setCPDefinitionInventoryEngine(
		String CPDefinitionInventoryEngine);

	/**
	 * Returns the low stock activity of this cp definition inventory.
	 *
	 * @return the low stock activity of this cp definition inventory
	 */
	@AutoEscape
	public String getLowStockActivity();

	/**
	 * Sets the low stock activity of this cp definition inventory.
	 *
	 * @param lowStockActivity the low stock activity of this cp definition inventory
	 */
	public void setLowStockActivity(String lowStockActivity);

	/**
	 * Returns the display availability of this cp definition inventory.
	 *
	 * @return the display availability of this cp definition inventory
	 */
	public boolean getDisplayAvailability();

	/**
	 * Returns <code>true</code> if this cp definition inventory is display availability.
	 *
	 * @return <code>true</code> if this cp definition inventory is display availability; <code>false</code> otherwise
	 */
	public boolean isDisplayAvailability();

	/**
	 * Sets whether this cp definition inventory is display availability.
	 *
	 * @param displayAvailability the display availability of this cp definition inventory
	 */
	public void setDisplayAvailability(boolean displayAvailability);

	/**
	 * Returns the display stock quantity of this cp definition inventory.
	 *
	 * @return the display stock quantity of this cp definition inventory
	 */
	public boolean getDisplayStockQuantity();

	/**
	 * Returns <code>true</code> if this cp definition inventory is display stock quantity.
	 *
	 * @return <code>true</code> if this cp definition inventory is display stock quantity; <code>false</code> otherwise
	 */
	public boolean isDisplayStockQuantity();

	/**
	 * Sets whether this cp definition inventory is display stock quantity.
	 *
	 * @param displayStockQuantity the display stock quantity of this cp definition inventory
	 */
	public void setDisplayStockQuantity(boolean displayStockQuantity);

	/**
	 * Returns the min stock quantity of this cp definition inventory.
	 *
	 * @return the min stock quantity of this cp definition inventory
	 */
	public int getMinStockQuantity();

	/**
	 * Sets the min stock quantity of this cp definition inventory.
	 *
	 * @param minStockQuantity the min stock quantity of this cp definition inventory
	 */
	public void setMinStockQuantity(int minStockQuantity);

	/**
	 * Returns the back orders of this cp definition inventory.
	 *
	 * @return the back orders of this cp definition inventory
	 */
	public boolean getBackOrders();

	/**
	 * Returns <code>true</code> if this cp definition inventory is back orders.
	 *
	 * @return <code>true</code> if this cp definition inventory is back orders; <code>false</code> otherwise
	 */
	public boolean isBackOrders();

	/**
	 * Sets whether this cp definition inventory is back orders.
	 *
	 * @param backOrders the back orders of this cp definition inventory
	 */
	public void setBackOrders(boolean backOrders);

	/**
	 * Returns the min order quantity of this cp definition inventory.
	 *
	 * @return the min order quantity of this cp definition inventory
	 */
	public int getMinOrderQuantity();

	/**
	 * Sets the min order quantity of this cp definition inventory.
	 *
	 * @param minOrderQuantity the min order quantity of this cp definition inventory
	 */
	public void setMinOrderQuantity(int minOrderQuantity);

	/**
	 * Returns the max order quantity of this cp definition inventory.
	 *
	 * @return the max order quantity of this cp definition inventory
	 */
	public int getMaxOrderQuantity();

	/**
	 * Sets the max order quantity of this cp definition inventory.
	 *
	 * @param maxOrderQuantity the max order quantity of this cp definition inventory
	 */
	public void setMaxOrderQuantity(int maxOrderQuantity);

	/**
	 * Returns the allowed order quantities of this cp definition inventory.
	 *
	 * @return the allowed order quantities of this cp definition inventory
	 */
	@AutoEscape
	public String getAllowedOrderQuantities();

	/**
	 * Sets the allowed order quantities of this cp definition inventory.
	 *
	 * @param allowedOrderQuantities the allowed order quantities of this cp definition inventory
	 */
	public void setAllowedOrderQuantities(String allowedOrderQuantities);

	/**
	 * Returns the multiple order quantity of this cp definition inventory.
	 *
	 * @return the multiple order quantity of this cp definition inventory
	 */
	public int getMultipleOrderQuantity();

	/**
	 * Sets the multiple order quantity of this cp definition inventory.
	 *
	 * @param multipleOrderQuantity the multiple order quantity of this cp definition inventory
	 */
	public void setMultipleOrderQuantity(int multipleOrderQuantity);

}