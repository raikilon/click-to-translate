import { CommonModule } from '@angular/common';
import {
  Component,
  Directive,
  TemplateRef,
  contentChild,
  computed,
  input,
  linkedSignal,
  signal,
} from '@angular/core';

export interface EditableFieldEditorContext {
  value: string;
  setValue: (value: string) => void;
  disabled: boolean;
}

@Directive({
  selector: 'ng-template[editableFieldEditor]',
  standalone: true,
})
export class EditableFieldEditorDirective {
  constructor(readonly templateRef: TemplateRef<EditableFieldEditorContext>) {}

  static ngTemplateContextGuard(
    _directive: EditableFieldEditorDirective,
    _context: unknown,
  ): _context is EditableFieldEditorContext {
    return true;
  }
}

@Component({
  selector: 'app-editable-field',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './editable-field.component.html',
})
export class EditableFieldComponent {
  readonly value = input.required<string>();
  readonly saveAction = input.required<(value: string) => Promise<void>>();
  readonly editorTemplate = input<TemplateRef<EditableFieldEditorContext> | null>(null);
  readonly emptyValueText = input('No value');
  readonly editLabel = input('Edit');
  readonly saveLabel = input('Save');
  readonly cancelLabel = input('Cancel');
  readonly projectedEditorTemplate = contentChild<
    EditableFieldEditorDirective,
    TemplateRef<EditableFieldEditorContext>
  >(EditableFieldEditorDirective, { read: TemplateRef });

  protected readonly editing = signal(false);
  protected readonly draft = linkedSignal<{ value: string; editing: boolean }, string>({
    source: () => ({
      value: this.value(),
      editing: this.editing(),
    }),
    computation: (source, previous) =>
      source.editing ? (previous?.value ?? source.value) : source.value,
  });
  protected readonly saving = signal(false);
  protected readonly errorMessage = signal<string | null>(null);

  protected readonly hasValue = computed(() => this.value().trim().length > 0);
  protected readonly canSave = computed(() => {
    const nextValue = this.draft().trim();
    return nextValue.length > 0 && nextValue !== this.value().trim();
  });

  protected readonly editorContext = computed<EditableFieldEditorContext>(() => ({
    value: this.draft(),
    setValue: (value: string) => this.draft.set(value),
    disabled: this.saving(),
  }));

  protected readonly effectiveEditorTemplate = computed(
    () => this.projectedEditorTemplate() ?? this.editorTemplate(),
  );

  beginEdit(): void {
    this.errorMessage.set(null);
    this.editing.set(true);
  }

  cancelEdit(): void {
    this.errorMessage.set(null);
    this.editing.set(false);
  }

  updateDraft(event: Event): void {
    const target = event.target;
    if (!(target instanceof HTMLInputElement)) {
      return;
    }

    this.draft.set(target.value);
  }

  async save(): Promise<void> {
    const nextValue = this.draft().trim();
    if (!nextValue) {
      return;
    }

    if (nextValue === this.value().trim()) {
      this.editing.set(false);
      this.errorMessage.set(null);
      return;
    }

    this.saving.set(true);
    this.errorMessage.set(null);

    try {
      await this.saveAction()(nextValue);
      this.editing.set(false);
    } catch (error) {
      this.errorMessage.set(this.asErrorMessage(error));
    } finally {
      this.saving.set(false);
    }
  }

  private asErrorMessage(error: unknown): string {
    if (error instanceof Error && error.message.trim()) {
      return error.message;
    }

    return 'Save failed. Please try again.';
  }
}
