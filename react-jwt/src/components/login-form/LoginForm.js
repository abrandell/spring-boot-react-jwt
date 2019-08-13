import React, { Component } from 'react';
import {
  Button,
  Container,
  Form,
  Header
} from 'semantic-ui-react';
import PropTypes from 'prop-types';

const initialState = {
  username: '',
  password: '',
};

export class LoginForm extends Component {

  constructor(props) {
    super(props);
    this.state = initialState;
  }

  handleChange = (e, {name, value}) => {
    this.setState({
      [name]: value
    })
  };

  handleSubmit = () => {
    const {username, password} = this.state;
    this.setState(initialState);
    return this.props.onSubmit(username, password);
  };

  render() {
    const {username, password} = this.state;

    return (
      <Container text>
        <h3>Login</h3>
        <Form onSubmit={this.handleSubmit}>
          <Form.Group>
            <Form.Field>
              <label>username</label>
              <Form.Input
                placeholder={'username'}
                name={'username'}
                type={'text'}
                value={username}
                onChange={this.handleChange}
              />
            </Form.Field>
          </Form.Group>
          <Form.Group>
            <Form.Field>
              <label>password</label>
              <Form.Input
                placeholder={'password'}
                name={'password'}
                type={'password'}
                value={password}
                onChange={this.handleChange}
              />
            </Form.Field>
          </Form.Group>
          <Button type='submit' primary>Login</Button>
        </Form>
        <Header><code>request: (POST /login)</code></Header>
        <pre style={{backgroundColor: 'lightgray', border: '1px solid black'}}>
          {JSON.stringify({username, password}, null, 2)}
        </pre>
      </Container>
    )
  }
}

LoginForm.propTypes = {
  onSubmit: PropTypes.func.isRequired
};

