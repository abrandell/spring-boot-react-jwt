import React, { Component } from 'react';
import { Account, LoginForm } from './components';
import { http } from './util/axios-config';
import { Button, Container, Header } from 'semantic-ui-react';

const initialState = {
  isAuthenticated: false,
  csrf: undefined,
  account: {
    username: undefined,
    id: undefined,
    authorities: undefined,
    uuid: undefined
  }
};


class App extends Component {

  constructor(props) {
    super(props);
    this.state = initialState;
  }

  login(username, password) {
    const params = {
      username: username,
      password: password
    };

    http.post('/login', params)
      .then(response => {
       http.get('/api/accounts/me')
          .then(response => {
            this.setState(ps => ({
              account: response.data,
              isAuthenticated: !ps.isAuthenticated,
              csrf: localStorage.getItem('csrf')
            }));
          })
      });
  }

  logout() {
    if (!this.state.isAuthenticated) {
      return new Error('Must be authenticated before logging out');
    }

    this.setState(initialState);

    localStorage.removeItem('csrf');
  }

  render() {
    const {account, isAuthenticated} = this.state;
    return (
      <Container text style={{marginTop: '5rem'}}>
        <Header>React JWT</Header>
        <LoginForm onSubmit={(u, p) => this.login(u, p)}/>
        <Account account={account}/>
        <Button disabled={!isAuthenticated} onClick={() => this.logout()}
                secondary>Logout</Button>
      </Container>
    );
  }
}

export default App;
