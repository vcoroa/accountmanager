package br.com.vcoroa.accountmanager.entities;

public enum TipoConta {
    A_PAGAR(1, "A Pagar"),
    A_RECEBER(2, "A Receber");

    private final int id;
    private final String descricao;

    TipoConta(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoConta getById(int id) {
        for(TipoConta tipo : values()) {
            if(tipo.id == id) return tipo;
        }
        throw new IllegalArgumentException("TipoConta inválido id: " + id);
    }
}