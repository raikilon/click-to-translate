import { HttpResourceRequest } from '@angular/common/http';

export interface TranslationLanguagesGateway {
  listLanguagesRequest(): HttpResourceRequest;
}
