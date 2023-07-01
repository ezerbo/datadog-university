import * as React from 'react';
import {initializeIcons} from '@fluentui/react';

initializeIcons();

export class Header extends React.Component {

   render() {
       return (
          <div>
              Datadog University. Learn about Observability using Infrastructure Monitoring, APM, NPM, Synthetic Monitoring, etc
          </div>
       );
   }
}
