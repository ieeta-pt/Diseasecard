import React from 'react';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;


const DashboardDefault = React.lazy(() => import('./features/dashboard/Dashboard'));
const AlertBoxPage = React.lazy(() => import('./features/endpointAlertBox/AlertBoxPage'));
const EndpointManagementPage = React.lazy(() => import('./features/endpointManagement/EndpointManagementPage'));
const SourceManagementPage = React.lazy(() => import('./features/sourcesManagement/SourceManagementPage'));
const SourceMapPage = React.lazy(() => import('./features/sourcesMap/SourceMapPage'));
const SPARQLEndpoint = React.lazy(() => import('./features/sparqlEndpoint/SPARQLEndpoint'));


const routes = [
    { path: '/dashboard/default', exact: true, name: 'Default', component: DashboardDefault },
    { path: '/utils/sparql', exact: true, name: 'SPARQL Endpoint', component: SPARQLEndpoint },
    { path: '/endpoint/alertbox', exact: true, name: 'AlertBox', component: AlertBoxPage },
    { path: '/endpoint/management', exact: true, name: 'Endpoints Management', component: EndpointManagementPage },
    { path: '/sources/management', exact: true, name: 'Sources Management', component: SourceManagementPage },
    { path: '/sources/map', exact: true, name: 'Sources Map', component: SourceMapPage },
];

export default routes;