/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SeloCartaoComponentsPage, SeloCartaoDeleteDialog, SeloCartaoUpdatePage } from './selo-cartao.page-object';

const expect = chai.expect;

describe('SeloCartao e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let seloCartaoUpdatePage: SeloCartaoUpdatePage;
    let seloCartaoComponentsPage: SeloCartaoComponentsPage;
    let seloCartaoDeleteDialog: SeloCartaoDeleteDialog;

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load SeloCartaos', async () => {
        await navBarPage.goToEntity('selo-cartao');
        seloCartaoComponentsPage = new SeloCartaoComponentsPage();
        await browser.wait(ec.visibilityOf(seloCartaoComponentsPage.title), 5000);
        expect(await seloCartaoComponentsPage.getTitle()).to.eq('efielApp.seloCartao.home.title');
    });

    it('should load create SeloCartao page', async () => {
        await seloCartaoComponentsPage.clickOnCreateButton();
        seloCartaoUpdatePage = new SeloCartaoUpdatePage();
        expect(await seloCartaoUpdatePage.getPageTitle()).to.eq('efielApp.seloCartao.home.createOrEditLabel');
        await seloCartaoUpdatePage.cancel();
    });

    it('should create and save SeloCartaos', async () => {
        const nbButtonsBeforeCreate = await seloCartaoComponentsPage.countDeleteButtons();

        await seloCartaoComponentsPage.clickOnCreateButton();
        await promise.all([
            seloCartaoUpdatePage.setDescricaoInput('descricao'),
            seloCartaoUpdatePage.setValorInput('5'),
            seloCartaoUpdatePage.tipoSelectLastOption(),
            seloCartaoUpdatePage.cartaoFidelidadeSelectLastOption()
        ]);
        expect(await seloCartaoUpdatePage.getDescricaoInput()).to.eq('descricao');
        expect(await seloCartaoUpdatePage.getValorInput()).to.eq('5');
        await seloCartaoUpdatePage.save();
        expect(await seloCartaoUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await seloCartaoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });

    it('should delete last SeloCartao', async () => {
        const nbButtonsBeforeDelete = await seloCartaoComponentsPage.countDeleteButtons();
        await seloCartaoComponentsPage.clickOnLastDeleteButton();

        seloCartaoDeleteDialog = new SeloCartaoDeleteDialog();
        expect(await seloCartaoDeleteDialog.getDialogTitle()).to.eq('efielApp.seloCartao.delete.question');
        await seloCartaoDeleteDialog.clickOnConfirmButton();

        expect(await seloCartaoComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
