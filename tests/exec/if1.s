	.text
	.globl main
main:
	subq $8, %rsp
	movq $65, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L38
	movq $66, %rdi
	movq %rdi, 0(%rsp)
L38:
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L30
	movq $0, %rdi
	cmpq $0, %rdi
	je L30
	movq $67, %rdi
	movq %rdi, 0(%rsp)
L30:
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L22
	movq $1, %rdi
	cmpq $0, %rdi
	je L22
	movq $68, %rdi
	movq %rdi, 0(%rsp)
L22:
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L18
	movq $69, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq 0(%rsp), %rdi
	cmpq $0, %rdi
	je L10
	movq $70, %rdi
	movq %rdi, 0(%rsp)
	movq 0(%rsp), %rdi
	call putchar
	movq $10, %rdi
	call putchar
	movq $0, %rax
	addq $8, %rsp
	ret
L10:
	movq $1, %rdi
	cmpq $0, %rdi
	je L6
	jmp L8
L18:
	movq $0, %rdi
	cmpq $0, %rdi
	je L14
	jmp L16
	jmp L22
	jmp L22
	jmp L30
	jmp L30
	jmp L38
	.data
