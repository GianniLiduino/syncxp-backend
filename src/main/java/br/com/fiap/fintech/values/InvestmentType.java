package br.com.fiap.fintech.values;

public enum InvestmentType {
    // Renda Fixa
    CDB("Certificado de Depósito Bancário", AssetClass.FIXED_INCOME),
    LCI("Letra de Crédito Imobiliário", AssetClass.FIXED_INCOME),
    LCA("Letra de Crédito do Agronegócio", AssetClass.FIXED_INCOME),
    TESOURO_DIRETO("Tesouro Direto", AssetClass.FIXED_INCOME),
    
    // Renda Variável
    STOCKS("Ações", AssetClass.VARIABLE_INCOME),
    FII("Fundos Imobiliários", AssetClass.VARIABLE_INCOME),
    CRYPTO("Criptomoedas", AssetClass.VARIABLE_INCOME);

    private final String description;
    private final AssetClass assetClass;

    InvestmentType(String description, AssetClass assetClass) {
        this.description = description;
        this.assetClass = assetClass;
    }

    public String getDescription() {
        return description;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }
}
