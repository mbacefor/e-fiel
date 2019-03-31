import { element, by, ElementFinder } from 'protractor';

export class EmpresaComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-empresa div table .btn-danger'));
    title = element.all(by.css('jhi-empresa div h2#page-heading span')).first();

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

export class EmpresaUpdatePage {
    pageTitle = element(by.id('jhi-empresa-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nomeInput = element(by.id('field_nome'));
    cnpjInput = element(by.id('field_cnpj'));
    enderecoInput = element(by.id('field_endereco'));
    foneInput = element(by.id('field_fone'));
    criadorSelect = element(by.id('field_criador'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNomeInput(nome) {
        await this.nomeInput.sendKeys(nome);
    }

    async getNomeInput() {
        return this.nomeInput.getAttribute('value');
    }

    async setCnpjInput(cnpj) {
        await this.cnpjInput.sendKeys(cnpj);
    }

    async getCnpjInput() {
        return this.cnpjInput.getAttribute('value');
    }

    async setEnderecoInput(endereco) {
        await this.enderecoInput.sendKeys(endereco);
    }

    async getEnderecoInput() {
        return this.enderecoInput.getAttribute('value');
    }

    async setFoneInput(fone) {
        await this.foneInput.sendKeys(fone);
    }

    async getFoneInput() {
        return this.foneInput.getAttribute('value');
    }

    async criadorSelectLastOption() {
        await this.criadorSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async criadorSelectOption(option) {
        await this.criadorSelect.sendKeys(option);
    }

    getCriadorSelect(): ElementFinder {
        return this.criadorSelect;
    }

    async getCriadorSelectedOption() {
        return this.criadorSelect.element(by.css('option:checked')).getText();
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

export class EmpresaDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-empresa-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-empresa'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
