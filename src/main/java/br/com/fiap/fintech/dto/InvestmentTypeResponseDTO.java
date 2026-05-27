package br.com.fiap.fintech.dto;

import br.com.fiap.fintech.values.AssetClass;

public class InvestmentTypeResponseDTO {
    private String key;
    private String description;
    private AssetClass assetClass;

    public InvestmentTypeResponseDTO(String key, String description, AssetClass assetClass) {
        this.key = key;
        this.description = description;
        this.assetClass = assetClass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public AssetClass getAssetClass() {
        return assetClass;
    }

    public void setAssetClass(AssetClass assetClass) {
        this.assetClass = assetClass;
    }
}
