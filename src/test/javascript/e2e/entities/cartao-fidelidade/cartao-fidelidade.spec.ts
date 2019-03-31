/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CartaoFidelidadeComponentsPage, CartaoFidelidadeDeleteDialog, CartaoFidelidadeUpdatePage } from './cartao-fidelidade.page-object';

const expect = chai.expect;

describe('CartaoFidelidade e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let cartaoFidelidadeUpdatePage: CartaoFidelidadeUpdatePage;
    let cartaoFidelidadeComponentsPage: CartaoFidelidadeComponentsPage;
    let cartaoFidelidadeDeleteDialog: CartaoFidelidadeDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load CartaoFidelidades', async () => {
        await navBarPage.goToEntity('cartao-fidelidade');
        cartaoFidelidadeComponentsPage = new CartaoFidelidadeComponentsPage();
        await browser.wait(ec.visibilityOf(cartaoFidelidadeComponentsPage.title), 5000);
        expect(await cartaoFidelidadeComponentsPage.getTitle()).to.eq('efielApp.cartaoFidelidade.home.title');
    });

    it('should load create CartaoFidelidade page', async () => {
        await cartaoFidelidadeComponentsPage.clickOnCreateButton();
        cartaoFidelidadeUpdatePage = new CartaoFidelidadeUpdatePage();
        expect(await cartaoFidelidadeUpdatePage.getPageTitle()).to.eq('efielApp.cartaoFidelidade.home.createOrEditLabel');
        await cartaoFidelidadeUpdatePage.cancel();
    });

    it('should create and save CartaoFidelidades', async () => {
        const nbButtonsBeforeCreate = await cartaoFidelidadeComponentsPage.countDeleteButtons();

        await cartaoFidelidadeComponentsPage.clickOnCreateButton();
        await promise.all([
            cartaoFidelidadeUpdatePage.setDataCriacaoInput('2000-12-31'),
            cartaoFidelidadeUpdatePage.setDataPremioInput('2000-12-31'),
            cartaoFidelidadeUpdatePage.campanhaSelectLastOption(),
            cartaoFidelidadeUpdatePage.donoSelectLastOption()
        ]);
        expect(await cartaoFidelidadeUpdatePage.getDataCriacaoInput()).to.eq('2000-12-31');
        expect(await cartaoFidelidadeUpdatePage.getDataPremioInput()).to.eq('2000-12-31');
        await cartaoFidelidadeUpdatePage.save();
        expect(await cartaoFidelidadeUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await cartaoFidelidadeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last CartaoFidelidade', async () => {
        const nbButtonsBeforeDelete = await cartaoFidelidadeComponentsPage.countDeleteButtons();
        await cartaoFidelidadeComponentsPage.clickOnLastDeleteButton();

        cartaoFidelidadeDeleteDialog = new CartaoFidelidadeDeleteDialog();
        expect(await cartaoFidelidadeDeleteDialog.getDialogTitle()).to.eq('efielApp.cartaoFidelidade.delete.question');
        await cartaoFidelidadeDeleteDialog.clickOnConfirmButton();

        expect(await cartaoFidelidadeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
