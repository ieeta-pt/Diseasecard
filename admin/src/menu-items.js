export default {
    items: [
        {
            id: 'navigation',
            title: 'Overall Statistics',
            type: 'group',
            icon: 'icon-navigation',
            children: [
                {
                    id: 'd',
                    title: 'Dashboard',
                    type: 'item',
                    url: '/dashboard/default',
                    icon: 'feather icon-home',
                }
            ]
        },
        {
            id: 'ui-element',
            title: 'Endpoints Management',
            type: 'group',
            icon: 'icon-ui',
            children: [
                {
                    id: 'basic',
                    title: 'AlertBox',
                    type: 'item',
                    url: '/endpoint/alertbox',
                    icon: 'feather icon-box'
                },
                {
                    id: 'endpointAlertBox',
                    title: 'Management',
                    type: 'item',
                    url: '/endpoint/management',
                    icon: 'feather icon-server'
                }
            ]
        },
        {
            id: 'utils',
            title: 'Utils',
            type: 'group',
            icon: 'icon-ui',
            children: [
                {
                    id: 'sparql',
                    title: 'SPARQL Endpoint',
                    type: 'item',
                    url: '/utils/sparql',
                    icon: 'feather icon-crosshair'
                }
            ]
        },
        {
            id: 'ui-forms',
            title: 'Source Management',
            type: 'group',
            icon: 'icon-group',
            children: [
                {
                    id: 'bootstrap',
                    title: 'Management',
                    type: 'item',
                    icon: 'feather icon-server',
                    url: '/sources/management'
                },
                {
                    id: 'maps',
                    title: 'Map',
                    type: 'item',
                    icon: 'feather icon-map',
                    url: '/sources/map'
                }
            ]
        }
    ]
}