import { element, by, ElementFinder } from 'protractor';

export class CampanhaComponentsPage {
    createButton = element(by.id('jh-create-entity'));
    deleteButtons = element.all(by.css('jhi-campanha div table .btn-danger'));
    title = element.all(by.css('jhi-campanha div h2#page-heading span')).first();

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

export class CampanhaUpdatePage {
    pageTitle = element(by.id('jhi-campanha-heading'));
    saveButton = element(by.id('save-entity'));
    cancelButton = element(by.id('cancel-save'));
    nomeInput = element(by.id('field_nome'));
    logoInput = element(by.id('file_logo'));
    premioInput = element(by.id('field_premio'));
    regrasInput = element(by.id('field_regras'));
    expiraInput = element(by.id('field_expira'));
    numeroSelosInput = element(by.id('field_numeroSelos'));
    empresaSelect = element(by.id('field_empresa'));

    async getPageTitle() {
        return this.pageTitle.getAttribute('jhiTranslate');
    }

    async setNomeInput(nome) {
        await this.nomeInput.sendKeys(nome);
    }

    async getNomeInput() {
        return this.nomeInput.getAttribute('value');
    }

    async setLogoInput(logo) {
        await this.logoInput.sendKeys(logo);
    }

    async getLogoInput() {
        return this.logoInput.getAttribute('value');
    }

    async setPremioInput(premio) {
        await this.premioInput.sendKeys(premio);
    }

    async getPremioInput() {
        return this.premioInput.getAttribute('value');
    }

    async setRegrasInput(regras) {
        await this.regrasInput.sendKeys(regras);
    }

    async getRegrasInput() {
        return this.regrasInput.getAttribute('value');
    }

    async setExpiraInput(expira) {
        await this.expiraInput.sendKeys(expira);
    }

    async getExpiraInput() {
        return this.expiraInput.getAttribute('value');
    }

    async setNumeroSelosInput(numeroSelos) {
        await this.numeroSelosInput.sendKeys(numeroSelos);
    }

    async getNumeroSelosInput() {
        return this.numeroSelosInput.getAttribute('value');
    }

    async empresaSelectLastOption() {
        await this.empresaSelect
            .all(by.tagName('option'))
            .last()
            .click();
    }

    async empresaSelectOption(option) {
        await this.empresaSelect.sendKeys(option);
    }

    getEmpresaSelect(): ElementFinder {
        return this.empresaSelect;
    }

    async getEmpresaSelectedOption() {
        return this.empresaSelect.element(by.css('option:checked')).getText();
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

export class CampanhaDeleteDialog {
    private dialogTitle = element(by.id('jhi-delete-campanha-heading'));
    private confirmButton = element(by.id('jhi-confirm-delete-campanha'));

    async getDialogTitle() {
        return this.dialogTitle.getAttribute('jhiTranslate');
    }

    async clickOnConfirmButton() {
        await this.confirmButton.click();
    }
}
