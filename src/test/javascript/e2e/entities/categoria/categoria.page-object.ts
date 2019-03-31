import { element, by, ElementFinder } from 'protractor';

export class CategoriaComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-categoria div table .btn-danger'));
    title = element.all(by.css('jhi-categoria div h2#page-heading span')).first();

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

export class CategoriaUpdatePage {
    pageTitle = element(by.id('jhi-categoria-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    descricaoInput = element(by.id('field_descricao'));
    ativoInput = element(by.id('field_ativo'));
    empresasSelect = element(by.id('field_empresas'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setDescricaoInput(descricao) {
        await this.descricaoInput.sendKeys(descricao);
    }

    async getDescricaoInput() {
        return this.descricaoInput.getAttribute('value');
    }

    getAtivoInput() {
        return this.ativoInput;
    }

    async empresasSelectLastOption() {
        await this.empresasSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async empresasSelectOption(option) {
        await this.empresasSelect.sendKeys(option);
    }

    getEmpresasSelect(): ElementFinder {
        return this.empresasSelect;
    }

    async getEmpresasSelectedOption() {
        return this.empresasSelect.element(by.css('option:checked')).getText();
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

export class CategoriaDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-categoria-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-categoria'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
