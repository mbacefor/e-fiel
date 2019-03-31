import { element, by, ElementFinder } from 'protractor';

export class CartaoFidelidadeComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-cartao-fidelidade div table .btn-danger'));
    title = element.all(by.css('jhi-cartao-fidelidade div h2#page-heading span')).first();

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

export class CartaoFidelidadeUpdatePage {
    pageTitle = element(by.id('jhi-cartao-fidelidade-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    dataCriacaoInput = element(by.id('field_dataCriacao'));
    dataPremioInput = element(by.id('field_dataPremio'));
    campanhaSelect = element(by.id('field_campanha'));
    donoSelect = element(by.id('field_dono'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDataCriacaoInput(dataCriacao) {
        await this.dataCriacaoInput.sendKeys(dataCriacao);
    }

    async getDataCriacaoInput() {
        return this.dataCriacaoInput.getAttribute('value');
    }

    async setDataPremioInput(dataPremio) {
        await this.dataPremioInput.sendKeys(dataPremio);
    }

    async getDataPremioInput() {
        return this.dataPremioInput.getAttribute('value');
    }

    async campanhaSelectLastOption() {
        await this.campanhaSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async campanhaSelectOption(option) {
        await this.campanhaSelect.sendKeys(option);
    }

    getCampanhaSelect(): ElementFinder {
        return this.campanhaSelect;
    }

    async getCampanhaSelectedOption() {
        return this.campanhaSelect.element(by.css('option:checked')).getText();
    }

    async donoSelectLastOption() {
        await this.donoSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async donoSelectOption(option) {
        await this.donoSelect.sendKeys(option);
    }

    getDonoSelect(): ElementFinder {
        return this.donoSelect;
    }

    async getDonoSelectedOption() {
        return this.donoSelect.element(by.css('option:checked')).getText();
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

export class CartaoFidelidadeDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-cartaoFidelidade-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-cartaoFidelidade'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
