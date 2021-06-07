import { configureStore } from '@reduxjs/toolkit';
import templateReducer from '../template/templateSlice';
import addSourceSlice from '../features/sourcesManagement/addSource/addSourceSlice';
import querySystemSlice from '../features/sparqlEndpoint/querySystem/querySystemSlice';
import listResourceSlice from '../features/sourcesManagement/listResources/listResourcesSlice';
import systemStatusSlice from "../features/sourcesManagement/systemStatus/systemStatusSlice";
import listPrefixesSlice from "../features/sparqlEndpoint/listPrefixes/listPrefixesSlice";
import endpointManagementSlice from "../features/endpointManagement/endpointManagementSlice";
import { getDefaultMiddleware } from '@reduxjs/toolkit'
import { reducer as formReducer } from 'redux-form';


const customizedMiddleware = getDefaultMiddleware({
  serializableCheck: false
})

export default configureStore({
  reducer: {
    template: templateReducer,
    addSource: addSourceSlice,
    querySystem: querySystemSlice,
    listResources: listResourceSlice,
    systemStatus: systemStatusSlice,
    form: formReducer,
    listPrefixes:listPrefixesSlice,
    endpointManagementURL:endpointManagementSlice,
  },
  middleware: customizedMiddleware
});
