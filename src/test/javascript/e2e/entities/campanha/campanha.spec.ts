/* tslint:disable no-unused-expression */
import { browser, ExpectedConditions as ec, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { CampanhaComponentsPage, CampanhaDeleteDialog, CampanhaUpdatePage } from './campanha.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Campanha e2e test', () => {
    let navBarPage: NavBarPage;
    let signInPage: SignInPage;
    let campanhaUpdatePage: CampanhaUpdatePage;
    let campanhaComponentsPage: CampanhaComponentsPage;
    /*let campanhaDeleteDialog: CampanhaDeleteDialog;*/
    const fileNameToUpload = 'logo-jhipster.png';
    const fileToUpload = '../../../../../main/webapp/content/images/' + fileNameToUpload;
    const absolutePath = path.resolve(__dirname, fileToUpload);

    before(async () => {
        await browser.get('/');
        navBarPage = new NavBarPage();
        signInPage = await navBarPage.getSignInPage();
        await signInPage.autoSignInUsing('admin', 'admin');
        await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
    });

    it('should load Campanhas', async () => {
        await navBarPage.goToEntity('campanha');
        campanhaComponentsPage = new CampanhaComponentsPage();
        await browser.wait(ec.visibilityOf(campanhaComponentsPage.title), 5000);
        expect(await campanhaComponentsPage.getTitle()).to.eq('efielApp.campanha.home.title');
    });

    it('should load create Campanha page', async () => {
        await campanhaComponentsPage.clickOnCreateButton();
        campanhaUpdatePage = new CampanhaUpdatePage();
        expect(await campanhaUpdatePage.getPageTitle()).to.eq('efielApp.campanha.home.createOrEditLabel');
        await campanhaUpdatePage.cancel();
    });

    /* it('should create and save Campanhas', async () => {
        const nbButtonsBeforeCreate = await campanhaComponentsPage.countDeleteButtons();

        await campanhaComponentsPage.clickOnCreateButton();
        await promise.all([
            campanhaUpdatePage.setNomeInput('nome'),
            campanhaUpdatePage.setLogoInput(absolutePath),
            campanhaUpdatePage.setPremioInput('premio'),
            campanhaUpdatePage.setRegrasInput('regras'),
            campanhaUpdatePage.setExpiraInput('2000-12-31'),
            campanhaUpdatePage.setNumeroSelosInput('5'),
            campanhaUpdatePage.empresaSelectLastOption(),
        ]);
        expect(await campanhaUpdatePage.getNomeInput()).to.eq('nome');
        expect(await campanhaUpdatePage.getLogoInput()).to.endsWith(fileNameToUpload);
        expect(await campanhaUpdatePage.getPremioInput()).to.eq('premio');
        expect(await campanhaUpdatePage.getRegrasInput()).to.eq('regras');
        expect(await campanhaUpdatePage.getExpiraInput()).to.eq('2000-12-31');
        expect(await campanhaUpdatePage.getNumeroSelosInput()).to.eq('5');
        await campanhaUpdatePage.save();
        expect(await campanhaUpdatePage.getSaveButton().isPresent()).to.be.false;

        expect(await campanhaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1);
    });*/

    /* it('should delete last Campanha', async () => {
        const nbButtonsBeforeDelete = await campanhaComponentsPage.countDeleteButtons();
        await campanhaComponentsPage.clickOnLastDeleteButton();

        campanhaDeleteDialog = new CampanhaDeleteDialog();
        expect(await campanhaDeleteDialog.getDialogTitle())
            .to.eq('efielApp.campanha.delete.question');
        await campanhaDeleteDialog.clickOnConfirmButton();

        expect(await campanhaComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
    });*/

    after(async () => {
        await navBarPage.autoSignOut();
    });
});
