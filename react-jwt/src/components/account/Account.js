import React from 'react';
import PropTypes from 'prop-types';
import { Container, Header } from 'semantic-ui-react';


export const Account = (props) => {
  return (
    <Container size="medium" textAlign="lefts" text>
      <Header>
        <code>response (GET /api/accounts/me):</code>
      </Header>
      <pre style={{backgroundColor: 'lightgray', border: '1px solid black'}}>
        {JSON.stringify(props.account, null, 2)}</pre>
    </Container>
  )
};

Account.propTypes = {
  account: PropTypes.shape({
    username: PropTypes.string.isRequired,
    id: PropTypes.number.isRequired,
    authorities: PropTypes.arrayOf(PropTypes.shape({
      id: PropTypes.number.isRequired,
      authority: PropTypes.string.isRequired
    })).isRequired,
    uuid: PropTypes.string
  }).isRequired
};
