import Ember from 'ember';

export default Ember.Controller.extend({

  queryParams: ['email', 'token'],

  email: null,
  token: null,

  newPassword: '',
  message: '',
  error: '',

  actions: {

    changePassword() {

      Ember.$.ajax({
        type: "POST",
        url: "http://localhost:8080/reset-password",

        data: {
          email: this.get('email'),
          token: this.get('token'),
          newPassword: this.get('newPassword')
        },

        success: () => {
          this.set('message', 'Password updated successfully!');
          this.set('error', '');
          this.set('newPassword', '');
          this.transitionToRoute('login');
        },

        error: (xhr) => {
          this.set('error', xhr.responseText);
          this.set('message', '');
          this.set('newPassword', '');
        }

      });
    }
  }

});




