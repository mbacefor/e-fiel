/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CategoriaComponentsPage, CategoriaDeleteDialog, CategoriaUpdatePage } from './categoria.page-object';

const expect = chai.expect;

describe('Categoria e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let categoriaUpdatePage: CategoriaUpdatePage;
    let categoriaComponentsPage: CategoriaComponentsPage;
    let categoriaDeleteDialog: CategoriaDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Categorias', async () => {
        await navBarPage.goToEntity('categoria');
        categoriaComponentsPage = new CategoriaComponentsPage();
        await browser.wait(ec.visibilityOf(categoriaComponentsPage.title), 5000);
        expect(await categoriaComponentsPage.getTitle()).to.eq('efielApp.categoria.home.title');
    });

    it('should load create Categoria page', async () => {
        await categoriaComponentsPage.clickOnCreateButton();
        categoriaUpdatePage = new CategoriaUpdatePage();
        expect(await categoriaUpdatePage.getPageTitle()).to.eq('efielApp.categoria.home.createOrEditLabel');
        await categoriaUpdatePage.cancel();
    });

    it('should create and save Categorias', async () => {
        const nbButtonsBeforeCreate = await categoriaComponentsPage.countDeleteButtons();

        await categoriaComponentsPage.clickOnCreateButton();
        await promise.all([
            categoriaUpdatePage.setDescricaoInput('descricao')
            // categoriaUpdatePage.empresasSelectLastOption(),
        ]);
        expect(await categoriaUpdatePage.getDescricaoInput()).to.eq('descricao');
        const selectedAtivo = categoriaUpdatePage.getAtivoInput();
        if (await selectedAtivo.isSelected()) {
            await categoriaUpdatePage.getAtivoInput().click();
            expect(await categoriaUpdatePage.getAtivoInput().isSelected()).to.be.false;
        } else {
            await categoriaUpdatePage.getAtivoInput().click();
            expect(await categoriaUpdatePage.getAtivoInput().isSelected()).to.be.true;
        }
        await categoriaUpdatePage.save();
        expect(await categoriaUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await categoriaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last Categoria', async () => {
        const nbButtonsBeforeDelete = await categoriaComponentsPage.countDeleteButtons();
        await categoriaComponentsPage.clickOnLastDeleteButton();

        categoriaDeleteDialog = new CategoriaDeleteDialog();
        expect(await categoriaDeleteDialog.getDialogTitle()).to.eq('efielApp.categoria.delete.question');
        await categoriaDeleteDialog.clickOnConfirmButton();

        expect(await categoriaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
