import React from 'react';
import $ from 'jquery';

window.jQuery = $;
window.$ = $;
global.jQuery = $;


const DashboardDefault = React.lazy(() => import('./features/dashboard/Dashboard'));
const AlertPage = React.lazy(() => import('./features/alertBox/AlertPage'));
const SourceManagementPage = React.lazy(() => import('./features/management/SourceManagementPage'));
const SourceMapPage = React.lazy(() => import('./features/map/SourceMapPage'));
const SourceReportPage = React.lazy(() => import('./features/report/SourceReportPage'));
const SPARQLEndpoint = React.lazy(() => import('./features/sparqlEndpoint/SPARQLEndpoint'));


const routes = [
    { path: '/dashboard/default', exact: true, name: 'Default', component: DashboardDefault },
    { path: '/alerts', exact: true, name: 'Alerts', component: AlertPage },
    { path: '/utils/sparql', exact: true, name: 'SPARQL Endpoint', component: SPARQLEndpoint },
    { path: '/sources/management', exact: true, name: 'Sources Management', component: SourceManagementPage },
    { path: '/sources/map', exact: true, name: 'Sources Map', component: SourceMapPage },
    { path: '/sources/report', exact: true, name: 'Sources Report', component: SourceReportPage },
];

export default routes;