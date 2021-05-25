import { configureStore } from '@reduxjs/toolkit';
import templateReducer from '../template/templateSlice';
import addSourceSlice from '../features/management/addSource/addSourceSlice';
import listResourceSlice from '../features/management/listResources/listResourcesSlice';
import systemStatusSlice from "../features/management/systemStatus/systemStatusSlice";
import { getDefaultMiddleware } from '@reduxjs/toolkit'

import { reducer as formReducer } from 'redux-form';


const customizedMiddleware = getDefaultMiddleware({
  serializableCheck: false
})

export default configureStore({
  reducer: {
    template: templateReducer,
    addSource: addSourceSlice,
    listResources: listResourceSlice,
    systemStatus: systemStatusSlice,
    form: formReducer
  },
  middleware: customizedMiddleware
});
