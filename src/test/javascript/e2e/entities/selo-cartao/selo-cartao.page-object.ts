import { element, by, ElementFinder } from 'protractor';

export class SeloCartaoComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-selo-cartao div table .btn-danger'));
    title = element.all(by.css('jhi-selo-cartao div h2#page-heading span')).first();

    async clickOnCreateButton() {
        await this.createButton.click();
    }

    async clickOnLastDeleteButton() {
        await this.deleteButtons.last().click();
    }

    async countDeleteButtons() {
        return this.deleteButtons.count();
    }

    async getTitle() {
        return this.title.getAttribute('jhiTranslate');
    }
}

export class SeloCartaoUpdatePage {
    pageTitle = element(by.id('jhi-selo-cartao-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descricaoInput = element(by.id('field_descricao'));
    valorInput = element(by.id('field_valor'));
    tipoSelect = element(by.id('field_tipo'));
    cartaoFidelidadeSelect = element(by.id('field_cartaoFidelidade'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDescricaoInput(descricao) {
        await this.descricaoInput.sendKeys(descricao);
    }

    async getDescricaoInput() {
        return this.descricaoInput.getAttribute('value');
    }

    async setValorInput(valor) {
        await this.valorInput.sendKeys(valor);
    }

    async getValorInput() {
        return this.valorInput.getAttribute('value');
    }

    async setTipoSelect(tipo) {
        await this.tipoSelect.sendKeys(tipo);
    }

    async getTipoSelect() {
        return this.tipoSelect.element(by.css('option:checked')).getText();
    }

    async tipoSelectLastOption() {
        await this.tipoSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async cartaoFidelidadeSelectLastOption() {
        await this.cartaoFidelidadeSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async cartaoFidelidadeSelectOption(option) {
        await this.cartaoFidelidadeSelect.sendKeys(option);
    }

    getCartaoFidelidadeSelect(): ElementFinder {
        return this.cartaoFidelidadeSelect;
    }

    async getCartaoFidelidadeSelectedOption() {
        return this.cartaoFidelidadeSelect.element(by.css('option:checked')).getText();
    }

    async save() {
        await this.saveButton.click();
    }

    async cancel() {
        await this.cancelButton.click();
    }

    getSaveButton(): ElementFinder {
        return this.saveButton;
    }
}

export class SeloCartaoDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-seloCartao-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-seloCartao'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
