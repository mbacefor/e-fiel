/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { EmpresaComponentsPage, EmpresaDeleteDialog, EmpresaUpdatePage } from './empresa.page-object';

const expect = chai.expect;

describe('Empresa e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let empresaUpdatePage: EmpresaUpdatePage;
    let empresaComponentsPage: EmpresaComponentsPage;
    let empresaDeleteDialog: EmpresaDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Empresas', async () => {
        await navBarPage.goToEntity('empresa');
        empresaComponentsPage = new EmpresaComponentsPage();
        await browser.wait(ec.visibilityOf(empresaComponentsPage.title), 5000);
        expect(await empresaComponentsPage.getTitle()).to.eq('efielApp.empresa.home.title');
    });

    it('should load create Empresa page', async () => {
        await empresaComponentsPage.clickOnCreateButton();
        empresaUpdatePage = new EmpresaUpdatePage();
        expect(await empresaUpdatePage.getPageTitle()).to.eq('efielApp.empresa.home.createOrEditLabel');
        await empresaUpdatePage.cancel();
    });

    it('should create and save Empresas', async () => {
        const nbButtonsBeforeCreate = await empresaComponentsPage.countDeleteButtons();

        await empresaComponentsPage.clickOnCreateButton();
        await promise.all([
            empresaUpdatePage.setNomeInput('nome'),
            empresaUpdatePage.setCnpjInput('cnpj'),
            empresaUpdatePage.setEnderecoInput('endereco'),
            empresaUpdatePage.setFoneInput('fone'),
            empresaUpdatePage.criadorSelectLastOption()
        ]);
        expect(await empresaUpdatePage.getNomeInput()).to.eq('nome');
        expect(await empresaUpdatePage.getCnpjInput()).to.eq('cnpj');
        expect(await empresaUpdatePage.getEnderecoInput()).to.eq('endereco');
        expect(await empresaUpdatePage.getFoneInput()).to.eq('fone');
        await empresaUpdatePage.save();
        expect(await empresaUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await empresaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Empresa', async () => {
        const nbButtonsBeforeDelete = await empresaComponentsPage.countDeleteButtons();
        await empresaComponentsPage.clickOnLastDeleteButton();

        empresaDeleteDialog = new EmpresaDeleteDialog();
        expect(await empresaDeleteDialog.getDialogTitle()).to.eq('efielApp.empresa.delete.question');
        await empresaDeleteDialog.clickOnConfirmButton();

        expect(await empresaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
