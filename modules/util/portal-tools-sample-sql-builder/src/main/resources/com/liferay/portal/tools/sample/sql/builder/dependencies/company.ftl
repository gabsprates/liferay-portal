<#assign companyModel = dataFactory.newCompanyModel() />

${dataFactory.setCompanyId(companyModel.companyId)}

${dataFactory.toInsertSQL(companyModel)}

${dataFactory.toInsertSQL(dataFactory.newVirtualHostModel())}

<#list dataFactory.newPortalPreferencesModels() as portalPreferencesModel>
	${dataFactory.toInsertSQL(portalPreferencesModel)}
</#list>

${csvFileWriter.write("company", virtualHostModel.hostname + "," + companyModel.companyId + "\n")}